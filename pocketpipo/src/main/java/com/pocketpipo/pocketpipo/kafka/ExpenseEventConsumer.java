package com.pocketpipo.pocketpipo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.dto.ExpenseThresholdExceededEventDTO;

@Service
public class ExpenseEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ExpenseEventConsumer.class);

    @KafkaListener(
            topics = KafkaTopicsConfig.EXPENSE_THRESHOLD_EXCEEDED_TOPIC,
            groupId = "pocketpipo-notifications"
    )
    public void onExpenseThresholdExceeded(ExpenseThresholdExceededEventDTO event) {
        log.info(
                "Consumed ExpenseThresholdExceeded event. eventId={}, userId={}, expenseId={}, amount={}, threshold={}",
                event.getEventId(),
                event.getUserId(),
                event.getExpenseId(),
                event.getAmount(),
                event.getThreshold()
        );
    }
}