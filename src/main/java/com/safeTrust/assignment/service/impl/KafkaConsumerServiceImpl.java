package com.safeTrust.assignment.service.impl;

import com.safeTrust.assignment.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    @KafkaListener(topics = "contact-events", groupId = "contact-group")
    @Override
    public void listen(String message) {
        log.info("🔸 Received Kafka message: {}", message);
    }
}
