package com.xxy.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        properties.put("group.id", "xxy");
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        kafkaConsumer.subscribe(Collections.singleton("xxy"));
        while (true) {
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(1000L);
            for (ConsumerRecord<String, String> consumerRecord : poll) {
                System.out.println(consumerRecord.offset()+","+consumerRecord.value());
            }
        }
    }
}
