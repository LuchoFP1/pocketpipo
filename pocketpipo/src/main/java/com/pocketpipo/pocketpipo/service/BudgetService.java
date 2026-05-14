package com.pocketpipo.pocketpipo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.dto.CreateBudgetRequestDTO;
import com.pocketpipo.pocketpipo.entity.Budget;
import com.pocketpipo.pocketpipo.entity.Expense;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.repository.BudgetRepository;
import com.pocketpipo.pocketpipo.repository.ExpenseRepository;

@Service
public class BudgetService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final UserService userService;

    public BudgetService(BudgetRepository budgetRepository, UserService userService
        , ExpenseRepository expenseRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.userService = userService;
        this.expenseRepository = expenseRepository;
    }

    public Budget createBudget(CreateBudgetRequestDTO budgetRequestDTO) {
        User user = userService.getCurrentLoggedUser();
        Budget budget = new Budget(budgetRequestDTO.getName(), user, budgetRequestDTO.getMaxAmount(), budgetRequestDTO.getStartDate(), budgetRequestDTO.getEndDate());
        Budget savedBudget = budgetRepository.save(budget);

        List<Expense> expenses = expenseRepository
        .findByUserIdAndDateBetweenAndDeletedFalseAndBudgetIsNull(
            user.getId(),
            savedBudget.getStartDate(),
            savedBudget.getEndDate()
        );

        for (Expense expense : expenses) {
            expense.setBudget(savedBudget);
        }

        expenseRepository.saveAll(expenses);

    return savedBudget;
    }

    public void updateBudget(CreateBudgetRequestDTO budgetRequestDTO, long id) {
        Budget budget = this.getBudgetById(id);
        budget.setUpdatedAt(LocalDate.now());
        budget.setEndDate(budgetRequestDTO.getEndDate());
        budget.setStartDate(budgetRequestDTO.getStartDate());
        budget.setMaxAmount(budgetRequestDTO.getMaxAmount());
        budget.setName(budgetRequestDTO.getName());
        budgetRepository.save(budget);
    }

    public Budget getBudgetById(long budgetId) {
        User user = userService.getCurrentLoggedUser();
        return this.budgetRepository.findByIdAndUserId(budgetId, user.getId());
    }

    public void deleteBudget(long budgetId) {
        Budget budget = this.getBudgetById(budgetId);
        budget.setDeleted(true);
        this.budgetRepository.save(budget);
    }
}
