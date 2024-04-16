package com.example.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EmbeddedKafka
public class KafkaBroker {

    public static void main(String[] args) {
        SpringApplication.run(KafkaBroker.class, args);
    }

    @Bean
    EmbeddedKafkaBroker broker() {
        Map<String, String> properties = new HashMap<>();
        properties.put("auto.create.topics.enable", "true");
        properties.put("allow.auto.create.topics", "true");

        EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaZKBroker(1).kafkaPorts(9092)
                .brokerProperties(properties)
                .brokerListProperty("spring.kafka.bootstrap-servers");

        return embeddedKafkaBroker;
    }

}