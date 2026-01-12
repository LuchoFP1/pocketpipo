package com.pocketpipo.pocketpipo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.dto.ExpenseResponseDTO;
import com.pocketpipo.pocketpipo.entity.Expense;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.exception.ExpenseNotFoundException;
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
        User user = this.userService.getCurrentLoggedUser();
        Expense expense = new Expense(createExpenseRequestDTO.getDescription(),
        createExpenseRequestDTO.getAmount(),
        createExpenseRequestDTO.getDate(),
        user
        );
        this.expenseRepository.save(expense);
    }

    public List<Expense> getAllCurrentUserExpensesList() {
        User user = this.userService.getCurrentLoggedUser();
        return this.expenseRepository.findAllByUserIdAndDeletedFalse(user.getId());
    }

    @Transactional
    public void deleteExpenseById(Long expenseId) {
        User user = this.userService.getCurrentLoggedUser();
        Expense expense = this.expenseRepository.findByIdAndUserIdAndDeletedFalse(expenseId, user.getId());
        if (expense == null) {
            throw new ExpenseNotFoundException("The expense you are trying to delete does not exist");
        }
        expense.setDeleted(true);
        this.expenseRepository.save(expense);
    }



    // Helpers

    public ExpenseResponseDTO toExpenseResponseDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(), 
                expense.getDescription(),
                expense.getAmount(), 
                expense.getDate(),
                expense.getDeleted()
        );
    }
}
