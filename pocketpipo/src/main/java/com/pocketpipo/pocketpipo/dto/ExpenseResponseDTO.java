package com.pocketpipo.pocketpipo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private Boolean deleted;

    

    public ExpenseResponseDTO(Long id, String description, BigDecimal amount, LocalDate date, Boolean deleted) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.deleted = deleted;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    
}
