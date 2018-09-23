package localtest

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD._

/**
  * Created by GP39 on 2016/8/12.
  */
object SparkWordCount {
  def main(args: Array[String]): Unit = {
    val sc  = new SparkContext(new SparkConf().setAppName("spark count"))
    val threshold  = args(1).toInt
    val tokenized = sc.textFile(args(0)).flatMap(_.split(" "))
    val wordCounts = tokenized.map((_,1)).reduceByKey(_ + _)
    val filtered =wordCounts.filter(_._2 >= threshold)
    val charCount = filtered.flatMap(_._1.toCharArray).map((_,1)).reduceByKey(_ + _)
    System.out.println(charCount.collect().mkString(" ,"))
  }

}
