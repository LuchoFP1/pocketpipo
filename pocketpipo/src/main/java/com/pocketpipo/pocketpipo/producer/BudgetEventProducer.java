package com.pocketpipo.pocketpipo.producer;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.pocketpipo.pocketpipo.event.BudgetThresholdExceededEvent;

@Component
public class BudgetEventProducer {

    private static final String BUDGET_THRESHOLD_EXCEEDED_TOPIC = "budget-threshold-exceeded";

    private final KafkaTemplate<String, BudgetThresholdExceededEvent> kafkaTemplate;

    public BudgetEventProducer(KafkaTemplate<String, BudgetThresholdExceededEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBudgetThresholdExceeded(
            Long userId,
            Long budgetId,
            String budgetName,
            Long expenseId,
            BigDecimal expenseAmount,
            BigDecimal budgetMaxAmount,
            int thresholdPercentage,
            LocalDate expenseDate
    ) {
        BudgetThresholdExceededEvent event = new BudgetThresholdExceededEvent(
                userId,
                budgetId,
                budgetName,
                expenseId,
                expenseAmount,
                thresholdPercentage,
                expenseDate
        );

        kafkaTemplate.send(BUDGET_THRESHOLD_EXCEEDED_TOPIC, String.valueOf(budgetId), event);
    }
}