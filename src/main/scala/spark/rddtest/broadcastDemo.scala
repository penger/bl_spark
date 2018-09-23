package spark.rddtest

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD._
/**
  * Created by gongp on 2018/9/20.
  */
class broadcastDemo {
  val sc= new SparkContext()
  sc.textFile("file:///opt/hadoop/logs/mapred-be-historyserver-node2.log.2").
    map(word => ( word.split(",")(0) ,word.split(",")(1))).reduceByKey(_+_).take(10).foreach(println)
}
