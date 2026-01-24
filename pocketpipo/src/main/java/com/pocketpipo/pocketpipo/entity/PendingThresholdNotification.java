package com.pocketpipo.pocketpipo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "pending_threshold_notifications",
        indexes = {
                @Index(name = "idx_pending_notif_user_sent", columnList = "userId,sentAt")
        }
)
public class PendingThresholdNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Para deduplicar (Kafka at-least-once)
    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long expenseId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal threshold;

    @Column(nullable = false)
    private Instant createdAt;

    // null => pendiente
    private Instant sentAt;

    protected PendingThresholdNotification() {
    }

    public PendingThresholdNotification(String eventId, Long userId, Long expenseId, BigDecimal amount, BigDecimal threshold) {
        this.eventId = eventId;
        this.userId = userId;
        this.expenseId = expenseId;
        this.amount = amount;
        this.threshold = threshold;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }
}