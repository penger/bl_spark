import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by GP39 on 2016/9/20.
  */
object SparkSQL2 {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","bl")
    val conf = new SparkConf().setAppName("spark sql").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val rowRdd = sc.textFile("hdfs://m78sit:8022/user/bl/b.q").filter(_.length >1).map(x=>(x,x.length)).sortByKey(true).saveAsTextFile("hdfs://m78sit:8022/user/bl/result")
    println(rowRdd)


  }

}
