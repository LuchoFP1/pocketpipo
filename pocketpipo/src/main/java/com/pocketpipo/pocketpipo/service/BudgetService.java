package com.pocketpipo.pocketpipo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pocketpipo.pocketpipo.dto.CreateBudgetRequestDTO;
import com.pocketpipo.pocketpipo.entity.Budget;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.repository.BudgetRepository;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserService userService;

    public BudgetService(BudgetRepository budgetRepository, UserService userService) {
        this.budgetRepository = budgetRepository;
        this.userService = userService;
    }

    public Budget createBudget(CreateBudgetRequestDTO budgetRequestDTO) {
        User user = userService.getCurrentLoggedUser();
        Budget budget = new Budget(budgetRequestDTO.getName(), user, budgetRequestDTO.getMaxAmount(), budgetRequestDTO.getStartDate(), budgetRequestDTO.getEndDate());
        return budgetRepository.save(budget);
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
