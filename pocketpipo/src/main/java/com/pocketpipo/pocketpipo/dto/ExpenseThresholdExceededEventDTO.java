package com.pocketpipo.pocketpipo.dto;

import java.math.BigDecimal;

public class ExpenseThresholdExceededEventDTO {
    private String eventId;
    private Long userId;
    private Long expenseId;
    private BigDecimal amount;
    private BigDecimal threshold;

    public ExpenseThresholdExceededEventDTO() {
    }

    public ExpenseThresholdExceededEventDTO(String eventId, Long userId, Long expenseId, BigDecimal amount,
            BigDecimal threshold) {
        this.eventId = eventId;
        this.userId = userId;
        this.expenseId = expenseId;
        this.amount = amount;
        this.threshold = threshold;
    }



    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getExpenseId() {
        return expenseId;
    }
    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getThreshold() {
        return threshold;
    }
    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    
}
