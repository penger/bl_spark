package spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gongp on 2018/9/26.
  * 得分最高的10部电影，看过电影最多的10个人
  * 女性看电影最多的10部电影
  * 男性看最多的10部电影
  */
object TopKMovieAnalyzer {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("top K")
    var dataPath= "data/ml-1m"
    if(args.length>0){
      dataPath=args(0)
    }else{
      conf.setMaster("local[1]")
    }
    val sc = new SparkContext(conf)

    val DATA_PATH = dataPath
    //得分最高的10部电影
    //UserID::MovieID::Rating::Timestamp
    val datasRDD = sc.textFile(DATA_PATH+"/ratings.dat")

    val cachedRating = datasRDD.map(_.split("::"))
      .map(x=>(x(0),x(1),x(2))).cache()

    val top10= cachedRating.map(x=>(x._2,(x._3.toInt ,1)))
      .reduceByKey((a,b)=>(a._1+b._1,a._2+b._2))
      .map(x=>(x._1,x._2._1.toFloat/x._2._2))
      .map(x=>(x._2,x._1))
      .sortByKey(false)
      .take(10)

    //MovieID::Title::Genres
    val moviesRDD = sc.textFile(DATA_PATH+"/movies.dat")
    val moviesMap = moviesRDD.map(x=>x.split("::")).map(x=>(x(0),x(1))).collect().toMap
    //打印得分最高的10部电影
    top10.foreach(x=>println(moviesMap.getOrElse(x._2,"xxxxxxx")))

    //看过电影最多的10个人
//    val usersRDD= sc.textFile(DATA_PATH+"/users.dat")
//    val userMap = usersRDD.map(_.split("::")).map(x=>(x(0),x(1)))
    cachedRating.map(x=>(x._1,1))
      .reduceByKey(_+_)
      .map(x=>(x._2,x._1))
      .sortByKey(false)
      .take(10)
      .foreach(println)


  }

}
