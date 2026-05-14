package com.pocketpipo.pocketpipo.repository;
import com.pocketpipo.pocketpipo.entity.Expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    public List<Expense> findAllByUserIdAndDeletedFalse(Long userId);
    public Expense findByIdAndUserIdAndDeletedFalse(Long expenseId, Long userId);

    public List<Expense> findAllByUserIdAndDeletedFalseAndDateGreaterThanEqualAndDateLessThan(Long userId, LocalDate startDate, LocalDate endDate);

    public List<Expense> findByUserIdAndDateBetweenAndDeletedFalseAndBudgetIsNull(long userId, LocalDate startDate, LocalDate endDate);

    public List<Expense> findByUserIdAndBudgetId(long userId, long budgetId);
}
