package spark.core

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.HashSet

/**
  * Created by gongp on 2018/9/26.
  * 年龄是18岁的最喜欢看的电影
  */
object PopularMovieAnalyzer {
  def main(args: Array[String]): Unit = {
    var dataPath= "data/ml-1m"
    val conf= new SparkConf().setAppName("popularMovieAnalyzer")
    if(args.length>0){
      dataPath=args(0)
    }else{
      conf.setMaster("local[1]")
    }
    val sc= new SparkContext(conf)
    val DATA_PATH= dataPath
    var USER_AGE="18"

    val usersRDD = sc.textFile(DATA_PATH+"/users.dat")
    val moviesRDD = sc.textFile(DATA_PATH+"/movies.dat")
    val ratingsRDD = sc.textFile(DATA_PATH+"/ratings.dat")
    //users: RDD[(userID,age)]
    val users = usersRDD.map(_.split("::")).map(x=>(x(0),x(2))).filter(_._2.equals(USER_AGE))

    val userlist  = users.map(_._1).collect()
    userlist.foreach(println)

    //broadcast
    val userSet = HashSet() ++ userlist
    val broadcastUserSet = sc.broadcast(userSet)

    //map side join rdd
    val topKmovies = ratingsRDD.map(_.split("::")).map(x=>(x(0),x(1)))
      .filter(x=>broadcastUserSet.value.contains(x._1))
      .map(x=>(x._2,1))
      .reduceByKey(_+_)
      .map(x=>(x._2,x._1))
      .sortByKey(false)
      .map(x=>(x._2,x._1))
      .take(10)

    /**
      * transfrom id to name
      */
    val movieID2name = moviesRDD.map(_.split("::")).map(x=>(x(0),x(1)))
      .collect().toMap

    topKmovies.map(x=>(movieID2name.getOrElse(x._1,null),x._2)).foreach(println)

    sc.stop()

  }

}
