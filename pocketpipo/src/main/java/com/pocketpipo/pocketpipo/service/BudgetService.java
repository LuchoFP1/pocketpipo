package com.pocketpipo.pocketpipo.service;

import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.entity.Budget;
import com.pocketpipo.pocketpipo.repository.BudgetRepository;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }
}
