package com.tekerasoft.tekeramarketplace.kafka;

import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "javaguides", groupId = "tekeraGroup")
    public void consume(String message) {
        LOGGER.info("Receive message from topic {}", message);
    }
}
