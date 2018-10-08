package spark.util

/**
  * Created by gongp on 2018/9/26.
  */
object KafkaRedisProperties {
  val REDIS_SERVER:String = "node1"
  val REDIS_PORT:Int = 6379
//  val

  val KAFKA_SERVER:String = "node2"
  val KAFAK_ADDR:String = KAFKA_SERVER+":9092"
  val KAFKA_USER_TOPIC:String = "user_events"
  val KAFKA_RECO_TOPIC:String = "reco6"
}
