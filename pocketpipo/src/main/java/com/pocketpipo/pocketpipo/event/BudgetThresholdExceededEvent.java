package com.pocketpipo.pocketpipo.event;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetThresholdExceededEvent {

    private Long userId;
    private Long budgetId;
    private String budgetName;
    private Long expenseId;
    private BigDecimal budgetMaxAmount;
    private int thresholdPercentage;
    private LocalDate expenseDate;

    public BudgetThresholdExceededEvent() {
    }

    public BudgetThresholdExceededEvent(
            Long userId,
            Long budgetId,
            String budgetName,
            Long expenseId,
            BigDecimal budgetMaxAmount,
            int thresholdPercentage,
            LocalDate expenseDate
    ) {
        this.userId = userId;
        this.budgetId = budgetId;
        this.budgetName = budgetName;
        this.expenseId = expenseId;
        this.budgetMaxAmount = budgetMaxAmount;
        this.thresholdPercentage = thresholdPercentage;
        this.expenseDate = expenseDate;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public BigDecimal getBudgetMaxAmount() {
        return budgetMaxAmount;
    }

    public int getThresholdPercentage() {
        return thresholdPercentage;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }
}