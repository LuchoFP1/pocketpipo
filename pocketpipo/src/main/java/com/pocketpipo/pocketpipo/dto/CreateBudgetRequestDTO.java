package com.pocketpipo.pocketpipo.dto;

import java.time.LocalDate;

public class CreateBudgetRequestDTO {
    private long id;
    private String name;
    private long maxAmount;
    private long userId;
    private LocalDate startDate;
    private LocalDate endDate;

    public CreateBudgetRequestDTO(String name, long maxAmount, long userId, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
