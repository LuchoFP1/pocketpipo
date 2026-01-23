package com.pocketpipo.pocketpipo.kafka;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.dto.ExpenseThresholdExceededEventDTO;

@Service
public class ExpenseEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ExpenseEventProducer.class);

    private final KafkaTemplate<String, ExpenseThresholdExceededEventDTO> kafkaTemplate;

    public ExpenseEventProducer(
            KafkaTemplate<String, ExpenseThresholdExceededEventDTO> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishExpenseThresholdExceeded(
            Long userId,
            Long expenseId,
            BigDecimal amount,
            BigDecimal threshold
    ) {
        ExpenseThresholdExceededEventDTO event =
                new ExpenseThresholdExceededEventDTO(
                        UUID.randomUUID().toString(),
                        userId,
                        expenseId,
                        amount,
                        threshold
                );

        log.info(
                "Publishing ExpenseThresholdExceeded event. expenseId={}, userId={}, amount={}",
                expenseId,
                userId,
                amount
        );

        kafkaTemplate.send(
                KafkaTopicsConfig.EXPENSE_THRESHOLD_EXCEEDED_TOPIC,
                event.getEventId(),
                event
        );
    }
}