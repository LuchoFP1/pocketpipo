package com.pocketpipo.pocketpipo.scheduler;

import com.pocketpipo.pocketpipo.entity.PendingThresholdNotification;
import com.pocketpipo.pocketpipo.repository.PendingThresholdNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PendingThresholdNotificationScheduler {

    private static final Logger log = LoggerFactory.getLogger(PendingThresholdNotificationScheduler.class);

    private final PendingThresholdNotificationRepository pendingRepo;

    public PendingThresholdNotificationScheduler(PendingThresholdNotificationRepository pendingRepo) {
        this.pendingRepo = pendingRepo;
    }

    @Scheduled(cron = "0 */1 * * * *", zone = "America/Argentina/Buenos_Aires")
    public void processPendingNotifications() {
        List<PendingThresholdNotification> pendingNotifications = pendingRepo.findAllBySentAtIsNull();

        if (pendingNotifications.isEmpty()) {
            log.info("No pending threshold notifications to process.");
            return;
        }

        Map<Long, List<PendingThresholdNotification>> notificationsByUserId =
                pendingNotifications.stream()
                        .collect(Collectors.groupingBy(PendingThresholdNotification::getUserId));

        notificationsByUserId.forEach((userId, notifications) ->
                log.info(
                        "Pending threshold notifications grouped for user. userId={}, pendingCount={}",
                        userId,
                        notifications.size()
                )
        );
    }
}