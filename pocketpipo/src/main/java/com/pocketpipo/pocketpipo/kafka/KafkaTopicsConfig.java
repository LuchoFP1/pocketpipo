package com.pocketpipo.pocketpipo.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    public static final String EXPENSE_THRESHOLD_EXCEEDED_TOPIC = "expense-threshold-exceeded";

    @Bean
    public NewTopic expenseThresholdExceededTopic() {
        return TopicBuilder.name(EXPENSE_THRESHOLD_EXCEEDED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}