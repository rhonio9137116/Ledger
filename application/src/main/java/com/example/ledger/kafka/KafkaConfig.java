package com.example.ledger.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${topic.postingEventRequest.name}")
    private String postingEventRequestTopicName;

    @Value("${topic.postingEventProvided.name}")
    private String postingEventProvided;

    @Value("${topic.assetPriceEventProvided.name}")
    private String assetPriceEventProvidedTopicName;

    @Value("${topic.walletBalanceEventProvided.name}")
    private String walletBalanceEventProvidedTopicName;


    @Value("${topic.partitions-num}")
    private Integer partitions;

    @Value("${topic.replication-factor}")
    private short replicationFactor;


    @Bean
    NewTopic PostingEventRequestTopic() {
        return new NewTopic(postingEventRequestTopicName, partitions, replicationFactor);
    }

    @Bean
    NewTopic PostingEventProvidedTopic() {
        return new NewTopic(postingEventProvided, partitions, replicationFactor);
    }

    @Bean
    NewTopic AssetPriceEventProvidedTopic() {
        return new NewTopic(assetPriceEventProvidedTopicName, partitions, replicationFactor);
    }

    @Bean
    NewTopic walletBalanceEventProvidedTopic() {
        return new NewTopic(walletBalanceEventProvidedTopicName, partitions, replicationFactor);
    }

    @Autowired
    private ProducerFactory<String, GenericRecord> producerFactory;
    @Autowired
    private ConsumerFactory<String, GenericRecord> consumerFactory;

    @Bean
    public KafkaTemplate<String, GenericRecord> kafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs()));
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>(producerFactory.getConfigurationProperties());
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return properties;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GenericRecord> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GenericRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        factory.setCommonErrorHandler(errorHandler());
        factory.getContainerProperties().setAckCount(1);
        factory.setConcurrency(1);
        return factory;
    }

    private CommonErrorHandler errorHandler() {
        int interval = 1;
        int maxRetryAttempts = 0;
        FixedBackOff fixedBackOff = new FixedBackOff(interval, maxRetryAttempts);
        return new DefaultErrorHandler((consumerRecord, exception) -> {
        }, fixedBackOff);
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = new HashMap<>(consumerFactory.getConfigurationProperties());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1);
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, List.of(CooperativeStickyAssignor.class.getName()));
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return properties;
    }


}
