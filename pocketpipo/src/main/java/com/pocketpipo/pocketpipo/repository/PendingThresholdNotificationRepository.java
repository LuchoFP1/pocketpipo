package com.pocketpipo.pocketpipo.repository;

import com.pocketpipo.pocketpipo.entity.PendingThresholdNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PendingThresholdNotificationRepository extends JpaRepository<PendingThresholdNotification, Long> {

    Optional<PendingThresholdNotification> findByEventId(String eventId);

    List<PendingThresholdNotification> findAllBySentAtIsNull();
}