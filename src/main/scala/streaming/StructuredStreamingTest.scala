package streaming

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  * Created by gongp on 2018/9/25.
  */
object StructuredStreamingTest {

  def main(args: Array[String]): Unit = {

    val sparkconf = new SparkConf();
    sparkconf.setMaster("local[1]")

    val spark = SparkSession.builder().appName("test spark streaming").config(sparkconf).getOrCreate()

    import spark.implicits._

    val lines = spark.readStream.format("socket").option("host","localhost").option("port",9999).load()

    val words= lines.as[String].flatMap(_.split(" "))

    val wordCounts = words.groupBy("value").count()

    val query = wordCounts.writeStream.outputMode("complete").format("console").start()

    query.awaitTermination()

  }

}
