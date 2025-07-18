package org.yunya.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${weather.topic}")
    private String weatherTopic;

    @Bean
    public NewTopic weatherTopic() {
        return TopicBuilder.name(weatherTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}