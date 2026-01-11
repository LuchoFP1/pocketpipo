package com.pocketpipo.pocketpipo.repository;
import com.pocketpipo.pocketpipo.entity.Expense;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    public List<Expense> findAllByUserIdAndDeletedFalse(Long userId);
}
