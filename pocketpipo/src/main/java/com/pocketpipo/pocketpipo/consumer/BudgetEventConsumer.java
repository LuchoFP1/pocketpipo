package com.pocketpipo.pocketpipo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.pocketpipo.pocketpipo.event.BudgetThresholdExceededEvent;

@Component
public class BudgetEventConsumer {

    @KafkaListener(
        topics = "budget-threshold-exceeded",
        groupId = "pocketpipo-budget-alerts"
    )
    public void consumeBudgetThresholdExceeded(BudgetThresholdExceededEvent event) {
        System.out.println(
            "Budget threshold exceeded. User ID: " + event.getUserId()
            + ", Budget ID: " + event.getBudgetId()
            + ", Budget name: " + event.getBudgetName()
            + ", Expense ID: " + event.getExpenseId()
            + ", Threshold: " + event.getThresholdPercentage() + "%"
            + ", Expense date: " + event.getExpenseDate()
        );
    }
}