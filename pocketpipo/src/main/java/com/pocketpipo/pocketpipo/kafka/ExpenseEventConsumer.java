package com.pocketpipo.pocketpipo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.dto.ExpenseThresholdExceededEventDTO;
import com.pocketpipo.pocketpipo.entity.PendingThresholdNotification;
import com.pocketpipo.pocketpipo.repository.PendingThresholdNotificationRepository;

@Service
public class ExpenseEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ExpenseEventConsumer.class);

    private final PendingThresholdNotificationRepository pendingRepo;

    public ExpenseEventConsumer(PendingThresholdNotificationRepository pendingRepo) {
        this.pendingRepo = pendingRepo;
    }

    @KafkaListener(
            topics = KafkaTopicsConfig.EXPENSE_THRESHOLD_EXCEEDED_TOPIC,
            groupId = "pocketpipo-notifications"
    )
    public void onExpenseThresholdExceeded(ExpenseThresholdExceededEventDTO event) {

        if (pendingRepo.findByEventId(event.getEventId()).isPresent()) {
            log.info("Duplicate event ignored. eventId={}", event.getEventId());
            return;
        }

        PendingThresholdNotification pending = new PendingThresholdNotification(
                event.getEventId(),
                event.getUserId(),
                event.getExpenseId(),
                event.getAmount(),
                event.getThreshold()
        );

        pendingRepo.save(pending);

        log.info(
                "Pending notification stored. eventId={}, userId={}, expenseId={}, amount={}, threshold={}",
                event.getEventId(),
                event.getUserId(),
                event.getExpenseId(),
                event.getAmount(),
                event.getThreshold()
        );
    }
}