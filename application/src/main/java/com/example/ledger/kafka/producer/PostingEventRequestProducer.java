package com.example.ledger.kafka.producer;

import com.example.ledger.avro.PostingEventRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "PostingEventRequestProducer")
public class PostingEventRequestProducer {
    @Value("${topic.postingEventRequest.name}")
    private String topic;


    private KafkaTemplate<String, GenericRecord> kafkaTemplate;

    @Autowired
    public PostingEventRequestProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(PostingEventRequest postingEventRequest) {
        this.kafkaTemplate.send(this.topic, postingEventRequest.getUserId().toString(), postingEventRequest);
        log.info(String.format("Produced PostingEventRequest to Topic:%s -> %s", topic, postingEventRequest));
    }
}
