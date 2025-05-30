package com.tekerasoft.tekeramarketplace.kafka;

import com.tekerasoft.tekeramarketplace.dto.payload.TargetPicturePayload;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class JsonKafkaProducer {

    private static final Logger LOGGER = Logger.getLogger(JsonKafkaProducer.class.getName());

    private final KafkaTemplate<String, CreateTargetPictureRequest> kafkaTemplate;

    public JsonKafkaProducer(KafkaTemplate<String, CreateTargetPictureRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CreateTargetPictureRequest targetPicture) {
        LOGGER.info(String.format("Sending Message to Kafka: %s", targetPicture.toString()));
        Message<CreateTargetPictureRequest> message = MessageBuilder
                .withPayload(targetPicture)
                .setHeader(KafkaHeaders.TOPIC, "javaguides_json")
                .build();
        kafkaTemplate.send(message);
    }
}
