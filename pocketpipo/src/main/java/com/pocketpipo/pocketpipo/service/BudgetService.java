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
}
