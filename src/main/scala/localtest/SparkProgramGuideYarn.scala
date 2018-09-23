package localtest

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD._
/**
  * Created by GP39 on 2016/9/6.
  */
object SparkProgramGuideYarn {
  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setAppName("spark word count")
    val sc = new SparkContext(conf)

    val data = Array(1,23,4,5,6,7)
    val distData  = sc.parallelize(data)
    val answer = distData.reduce((a,b) => a+b)
    println(answer)


    val distFile = sc.textFile("a.q")
   distFile.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).sortByKey(false).saveAsTextFile("bbb")

  }

}
