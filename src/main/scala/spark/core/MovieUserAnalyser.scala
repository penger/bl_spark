package spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gongp on 2018/9/26.
  */
object MovieUserAnalyser {
  def main(args: Array[String]): Unit = {
    var dataPath= "data/ml-1m"
    val conf = new SparkConf().setAppName("movie analyzer")
    if(args.length > 0 ){
      dataPath=args(0)
    }else{
      conf.setMaster("local[1]")
    }

    val sc= new SparkContext(conf)
    val DATA_PATH=dataPath
    val MOVIE_TITLE = "Lord of the Rings, The(1978)"
    val MOVID_ID="2116"

    val usersRDD=sc.textFile(DATA_PATH+"/users.dat")
    val ratingsRDD =sc.textFile(DATA_PATH+"/ratings.dat")

    val users= usersRDD.map(_.split("::")).map(x=>(x(0),(x(1),x(2))))
    val ratings = ratingsRDD.map(_.split("::"))

    val usermovie = ratings.map(x=>(x(0),x(1))).filter(_._2.equals(MOVID_ID))
    usermovie.collect().take(10).foreach(println)

    val userRating = usermovie.join(users)

    val userDistribution= userRating.map{x=>(x._2._2,1)}.reduceByKey(_+_)
    userDistribution.foreach(println)
    sc.stop()

  }

}
