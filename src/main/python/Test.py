from kafka import KafkaConsumer
consumer = KafkaConsumer('test', group_id='test', bootstrap_servers='10.201.48.106:9092')
# consumer.assign([TopicPartition('test', 131)])
for message in consumer:
    print message

