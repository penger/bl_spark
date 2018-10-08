package spark.rddtest

import java.io.{File, FilenameFilter}
import java.util.UUID

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD._
/**
  * Created by gongp on 2018/9/20.
  */
object broadcastDemo {
//  val sc= new SparkContext()
//  sc.textFile("file:///opt/hadoop/logs/mapred-be-historyserver-node2.log.2").
//    map(word => ( word.split(",")(0) ,word.split(",")(1))).reduceByKey(_+_).take(10).foreach(println)

  def main(args: Array[String]): Unit = {
    for( i <- (1 to 100)){
    println(UUID.randomUUID())
    }
    val path = new File(".")
    val files = path.list(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = {
        if (name.contains("abc"))
          false
        else
          true
      }
    })
    files.foreach(x=>(println(x)))
  }


  //3b677c12-5c26-4710-9370-f9800aacfe9a


}
