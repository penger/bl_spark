package spark.streaming

import com.alibaba.fastjson.JSON
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import spark.util.{KafkaRedisProperties, RedisClient}


/**
  * Created by gongp on 2018/9/27.
  */
class UserClickCountAnalysis {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("user click analysis")
    if(args.length == 0 ){
      conf.setMaster("lcoal[1]")
    }

    val scc = new StreamingContext(conf,Seconds(5))
    val topics = KafkaRedisProperties.KAFKA_USER_TOPIC.split("\\,").toSet
    println("topic is " + topics)

    val brokers = KafkaRedisProperties.KAFAK_ADDR

    val kafkaParams = Map[String ,String ](
      "metadata.broker.list"->brokers,
      "serializer.class"->"kafka.serializer.StringEncoder"
    )

    val clickHashKey="app::user::click"

    val kafkaStream = KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](scc,kafkaParams,topics)
    val events = kafkaStream.flatMap(line=>{
      println(s"line $line .")
      val data =JSON.parseObject(line._2)
      Some(data)
    })

    val userClicks = events.map(x=>(x.getString("uid"),x.getLong("click_count"))).reduceByKey(_+_)
    userClicks.foreachRDD(rdd=>{
      rdd.foreachPartition(partitionOfRecords=>{
        val jedis = RedisClient.pool.getResource
        partitionOfRecords.foreach(pair=>{
          try {
            val uid = pair._1
            val clickCount = pair._2
            jedis.hincrBy(clickHashKey, uid, clickCount)
            println("update uid" + uid + "to " + clickCount)
          } catch {
            case e:Exception => println("error :"+e)
          }
        })
        jedis.close()
      })
    })

    scc.start()
    scc.awaitTermination()
  }


  def testJedis(): Unit ={
    val jedis = RedisClient.pool.getResource
    println(jedis.dbSize())
  }
}
