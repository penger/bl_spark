package localtest

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by GP39 on 2016/9/6.
  */
object SparkStreamingWordCount {
  def main(args: Array[String]): Unit = {
    if(args.length!=3){
      println("usage:<hostname><port><seconds>")
      System.exit(1)
    }

    //    StreamingExamples.setStreamingLogLevels()
    val ssc = new StreamingContext(new SparkConf,Seconds(args(2).toInt))
    //
    //    val ssc = new StreamingContext(args(0), "NetWorkWordCount" , Seconds(args(3).toInt),System.getenv("SPARK_HOME"))
    val lines =  ssc.socketTextStream(args(0),args(1).toInt,StorageLevel.MEMORY_ONLY_SER)

    //
    val words = lines.flatMap(_.split(" "))
    val wc = words.map(x => (x,1)).reduceByKey(_ + _)
    wc.print()
    ssc.start()
    ssc.awaitTermination()


  }

}
