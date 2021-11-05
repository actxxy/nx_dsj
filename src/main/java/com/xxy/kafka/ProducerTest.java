package com.xxy.kafka;

import kafka.server.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i < 100; i++) {
            ProducerRecord producerRecord = new ProducerRecord<String, String>("xxy", "hello world --" + String.valueOf(i));
            kafkaProducer.send(producerRecord);
        }

        kafkaProducer.close();
    }
}
