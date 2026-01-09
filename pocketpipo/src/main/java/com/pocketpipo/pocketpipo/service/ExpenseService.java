package com.pocketpipo.pocketpipo.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.entity.Expense;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.repository.ExpenseRepository;


@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseService(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }



    @Transactional
    public void createExpense(CreateExpenseRequestDTO createExpenseRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userService.getUserByEmail(email);
        Expense expense = new Expense(createExpenseRequestDTO.getDescription(),
        createExpenseRequestDTO.getAmount(),
        createExpenseRequestDTO.getDate(),
        user
        );
        this.expenseRepository.save(expense);
    }
}
