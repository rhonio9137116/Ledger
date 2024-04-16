package com.example.ledger.kafka.producer;

import com.example.ledger.avro.PostingEventProvided;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "PostingEventProvidedProducer")
public class PostingEventProvidedProducer {
    @Value("${topic.postingEventProvided.name}")
    private String topic;


    private KafkaTemplate<String, GenericRecord> kafkaTemplate;

    @Autowired
    public PostingEventProvidedProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PostingEventProvided postingEventProvided) {
        this.kafkaTemplate.send(this.topic, postingEventProvided.getUserId().toString(), postingEventProvided);
        log.info(String.format("Produced PostingEventProvided to Topic:%s -> %s", topic, postingEventProvided));
    }
}
