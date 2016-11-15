//package streaming
//
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
//
///**
//  * Created by GP39 on 2016/9/29.
//  */
//object KafkaStreaming {
//  def main(args: Array[String]): Unit = {
//    if(args.length <4 ){
//      println("usage: kafkawordcount <zkQuorum>,<group><topics><numThreads>")
//      System.exit(1)
//    }
//
//    val Array(zkQuorum,group,topics,numThreads) =args
//    val sparkConf = new SparkConf().setAppName("kafkawordcount")
//    val ssc = new StreamingContext(sparkConf,Seconds(2))
//    ssc.checkpoint("checkpoint");
//
//    val topicMap = topics.split(",").map((_,numThreads.toInt)).toMap
//    val lines = KafkaUtils.createStream(ssc,zkQuorum,group,topicMap).map(_._2)
//
//    val words = lines.flatMap(_.split(" "))
//    words.map(x=>(x,1)).reduceByKeyAndWindow(_+_,_-_,Minutes(10),Seconds(2),2).print()
//
//    ssc.start()
//    ssc.awaitTermination()
//
//
//  }
//}
