package com.pocketpipo.pocketpipo.repository;
import com.pocketpipo.pocketpipo.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
}
