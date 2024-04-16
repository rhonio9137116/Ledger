package com.example.ledger.assetPriceTicker;

import com.example.ledger.avro.AssetPriceEventProvided;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@CommonsLog(topic = "AssetPriceEventProvidedProducer")
public class AssetPriceEventProvidedProducer {
    @Value("${topic.assetPriceEventProvided.name}")
    private String topic;

    private KafkaTemplate<String, GenericRecord> kafkaTemplate;

    @Autowired
    public AssetPriceEventProvidedProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AssetPriceEventProvided assetPriceEventProvided) {
        this.kafkaTemplate.send(this.topic, assetPriceEventProvided.getAssetId().toString(), assetPriceEventProvided);
        log.info(String.format("Produced AssetPriceEventProvided to Topic:%s -> %s", topic, assetPriceEventProvided));
    }
}
