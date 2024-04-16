package com.example.ledger.kafka.producer;

import com.example.ledger.avro.WalletBalanceEventProvided;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "WalletBalanceEventProvidedProducer")
public class WalletBalanceEventProvidedProducer {
    @Value("${topic.walletBalanceEventProvided.name}")
    private String topic;


    private KafkaTemplate<String, GenericRecord> kafkaTemplate;

    @Autowired
    public WalletBalanceEventProvidedProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(WalletBalanceEventProvided walletBalanceEventProvided) {
        this.kafkaTemplate.send(this.topic, walletBalanceEventProvided.getUserId().toString(), walletBalanceEventProvided);
        log.info(String.format("Produced WalletBalanceEventProvided to Topic:%s -> %s", topic, walletBalanceEventProvided));
    }
}
