import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.omg.PortableInterceptor.INACTIVE;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by GP39 on 2016/9/29.
 */
public class KafkaJavaConsumer {
    public static void main(String[] args) {
        //配置文件
        Properties originalProps = new Properties();
        originalProps.put("zookeeper.connect", "10.201.48.104:2181");
//        originalProps.put("group.id", "234");
        originalProps.put("serializer.class", "kafka.serializer.StringEncoder");
        //创建消费连接
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(originalProps));

        //准备数据
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put("test", 1);
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        //创建topic的消息流
        Map<String, List<KafkaStream<String, String>>> topicMessageStreams = consumer.createMessageStreams(topicCountMap , keyDecoder, valueDecoder);
        //获取具体某个topic
        KafkaStream<String, String> kafkaStream = topicMessageStreams.get("test").get(0);
        //遍历输出
        ConsumerIterator<String, String> iterator = kafkaStream.iterator();
        while(iterator.hasNext()){
            MessageAndMetadata<String, String> next = iterator.next();
            System.out.println(next.message());
        }

    }

}
