package streaming

import java.util.Properties

/**
  * Created by GP39 on 2016/9/29.
  */
object KafkaProducer {

  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("metadata.broker.list","10.201.48.106:9092")
    props.put("serializer.class","kafka.serializer.StringEncoder")
//    props.put("key.")
  }

}
