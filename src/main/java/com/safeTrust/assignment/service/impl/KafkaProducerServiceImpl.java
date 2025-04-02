package com.safeTrust.assignment.service.impl;

import com.safeTrust.assignment.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "contact-events";

    @Override
    public void sendMessage(String message) {
        log.info("🔹 Sending message to Kafka: {}", message);
        kafkaTemplate.send(TOPIC, message);
    }
}
