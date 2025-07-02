package com.tekerasoft.tekeramarketplace.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.List;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public List<NewTopic> kafkaTopics() {
        return List.of(
                TopicBuilder.name("mindmap-processing-topic").build(),
                TopicBuilder.name("delete-image-processing").build()
//                TopicBuilder.name("resize-product-video").build()
        );
    }

}
