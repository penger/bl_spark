package spark.ml

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

/**
  * Created by gongp on 2018/9/28.
  */
object MovieLensALS {
  def main(args: Array[String]): Unit = {
    val dataPath = "data/ml-1m"
    val conf = new SparkConf().setMaster("local[1]").setAppName("movive lens ALS")
    val sc = new SparkContext(conf)

    val RATING_PATH = dataPath+ "/ratings.dat"
    val MOVIE_PATH = dataPath+"/movies.dat"

    val ratings = sc.textFile(RATING_PATH).map{line =>
      val fields = line.split("::")
      // format: ( timestamp%10 Rating(userId ,movieId, rating)
      (fields(3).toLong %10 ,Rating(fields(0).toInt , fields(1).toInt ,fields(2).toDouble))
    }

    val movies = sc.textFile(MOVIE_PATH).map{line=>
      val fields = line.split("::")
      //format (movieId ,movieName)
      (fields(0).toInt , fields(1))
    }.collect().toMap

    //
    val numRating = ratings.count()
    val numUsers = ratings.map(_._2.user).distinct().count()
    val numMovies = ratings.map(_._2.product).distinct.count()

    println(s"评分数量为 $numRating , 用户为 $numUsers , 电影数量为： $numMovies")

    //评分最高的电影
    val mostRatedMovieIDs = ratings.map(_._2.product)  //提取 电影的ID
      .countByValue()   //统计单个电影的评分
      .toSeq            //转换为seq
      .sortBy(- _._2)   //根据评分的数量
      .take(50)         //取出50个
      .map(_._1)        //获取ID

    val random = new Random(0)
    val selectedMovies = mostRatedMovieIDs.filter(x=>random.nextDouble()<0.3)
      .map(x=>(x,movies(x)))
      .toSeq
    s"打印排名前50的电影 中的 概率的0.3"
    selectedMovies.foreach(println)


    // elicitate ratings

    val myRatings = elicitateRatings(selectedMovies)
    val myRatingsRDD = sc.parallelize(myRatings, 1)

    val numPartitons = 20
    val training = ratings.filter(x=>x._1<6)
      .values
      .union(myRatingsRDD)
      .repartition(numPartitons)
      .persist()

    val validation = ratings.filter(x=>x._1 >=6&& x._1<8)
      .values
      .repartition(numPartitons)
      .persist

    val test = ratings.filter(x=>x._1>8).values.persist

    val trainingcount = training.count
    val testcount = test.count
    val validationcount = validation.count

    s"traing count is : $trainingcount testcount is :$testcount validatecount is :$validationcount"

    //训练模型
    val ranks= List(8,12)
    val lambdas = List(0.1 ,10.0)
    val numIters= List(10, 20)
    var bestModel :Option[MatrixFactorizationModel] =None
    var bestValidationRmse = Double.MaxValue
    var bestRank = 0
    var bestLambda = -1.0
    var bestNumIter = -1

    for(rank<-ranks; lambda <-lambdas ; numIter<-numIters){
      val model = ALS.train(training,rank,numIter,lambda)
      val validatoinRmse = computeRmse(model,validation,validationcount)
      println("RMSE(validation)="+validatoinRmse + "for the model trained with rank ="+rank
      +", lambda = "+ lambda + " and numIter is :"+ numIter)

      if(validatoinRmse<bestValidationRmse){
        bestModel=Some(model)
        bestValidationRmse = validatoinRmse
        bestRank = rank
        bestLambda = lambda
        bestNumIter = numIter
      }
    }

    //测试数据集上计算测试数据集
    val testRmse=computeRmse(bestModel.get ,test , testcount)
    println("the best model was trained with rank = "+bestRank +" and lambda= "+bestLambda +
    ", and numiter is "+ bestNumIter + " and its RMSE on the test set is "+testRmse)

    //创建自定义数据集，并在之上运行model
    val meanRating = training.union(validation).map(_.rating).mean()
    val baselineRmse = math.sqrt(test.map(x=>(meanRating-x.rating)*(meanRating-x.rating)).reduce(_+_)/testcount)
    val improvement = (baselineRmse-testRmse)/baselineRmse*100

    //自定义推荐
    val myRateMovieIds = myRatings.map(_.product).toSet
    val candidates = sc.parallelize(movies.keys.filter(!myRateMovieIds.contains(_)).toSeq)
    val recommandations = bestModel.get
      .predict(candidates.map((0,_)))
      .collect
      .sortBy(- _.rating)
      .take(50)

    var i =1
    println("movies recommand for you :")
    recommandations.foreach{r=>{
      println("%2d".format(i)+ movies(r.product))
      i+=1
    }}








  }






  /** Compute RMSE (Root Mean Squared Error). */
  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating], n: Long) = {
    val predictions: RDD[Rating] = model.predict(data.map(x => (x.user, x.product)))
    val predictionsAndRatings = predictions.map(x => ((x.user, x.product), x.rating))
      .join(data.map(x => ((x.user, x.product), x.rating)))
      .values
    math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).reduce(_ + _) / n)
  }


  /** Elicitate ratings from command-line. */
  def elicitateRatings(movies: Seq[(Int, String)]) = {
    val prompt = "Please rate the following movie (1-5 (best), or 0 if not seen):"
    println(prompt)
    val ratings = movies.flatMap { x =>
      var rating: Option[Rating] = None
      var valid = false
      while (!valid) {
        print(x._2 + ": ")
        try {
          val r = Console.readInt
          if (r < 0 || r > 5) {
            println(prompt)
          } else {
            valid = true
            if (r > 0) {
              rating = Some(Rating(0, x._1, r))
            }
          }
        } catch {
          case e: Exception => println(prompt)
        }
      }
      rating match {
        case Some(r) => Iterator(r)
        case None => Iterator.empty
      }
    }
    if(ratings.isEmpty) {
      error("No rating provided!")
    } else {
      ratings
    }
}
  }



