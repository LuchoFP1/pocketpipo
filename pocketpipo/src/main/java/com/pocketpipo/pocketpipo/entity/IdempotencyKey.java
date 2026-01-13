package com.pocketpipo.pocketpipo.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(
    name = "idempotency_keys",
    uniqueConstraints = {
        @UniqueConstraint(name = "ux_idem_user_op_key", columnNames = {"user_id", "operation", "idem_key"})
    }
)
public class IdempotencyKey {

    public enum Status {
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "idem_key", nullable = false, length = 255)
    private String idemKey;

    @Column(name = "operation", nullable = false, length = 100)
    private String operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @ManyToOne(optional = true)
    @JoinColumn(name = "resource_id")
    private Expense resource;

    @Column(name = "request_hash", length = 64)
    private String requestHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public IdempotencyKey() {}

    public IdempotencyKey(User user, String idemKey, String operation, Status status) {
        this.user = user;
        this.idemKey = idemKey;
        this.operation = operation;
        this.status = status;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // getters/setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getIdemKey() { return idemKey; }
    public void setIdemKey(String idemKey) { this.idemKey = idemKey; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Expense getResource() { return resource; }
    public void setResource(Expense resource) { this.resource = resource; }

    public String getRequestHash() { return requestHash; }
    public void setRequestHash(String requestHash) { this.requestHash = requestHash; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
