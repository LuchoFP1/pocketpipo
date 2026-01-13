package com.pocketpipo.pocketpipo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.dto.ExpenseResponseDTO;
import com.pocketpipo.pocketpipo.entity.Expense;
import com.pocketpipo.pocketpipo.entity.IdempotencyKey;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.exception.ExpenseNotFoundException;
import com.pocketpipo.pocketpipo.repository.ExpenseRepository;
import com.pocketpipo.pocketpipo.repository.IdempotencyKeyRepository;


@Service
public class ExpenseService {
    private static final String OP_CREATE_EXPENSE = "CREATE_EXPENSE";
    private final ExpenseRepository expenseRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final UserService userService;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            IdempotencyKeyRepository idempotencyKeyRepository,
            UserService userService
    ) {
        this.expenseRepository = expenseRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.userService = userService;
    }


   @Transactional
    public void createExpense(CreateExpenseRequestDTO dto, String idempotencyKey) {
        User user = this.userService.getCurrentLoggedUser();

        var existingKey =
                idempotencyKeyRepository.findByUserIdAndOperationAndIdemKey(
                        user.getId(),
                        OP_CREATE_EXPENSE,
                        idempotencyKey
                );

        if (existingKey.isPresent()) {
            if (existingKey.get().getStatus() == IdempotencyKey.Status.COMPLETED) {
                return;
            }
        }
        IdempotencyKey idem = new IdempotencyKey(
                user,
                idempotencyKey,
                OP_CREATE_EXPENSE,
                IdempotencyKey.Status.IN_PROGRESS
        );
        idempotencyKeyRepository.save(idem);
        Expense expense = new Expense(
                dto.getDescription(),
                dto.getAmount(),
                dto.getDate(),
                user
        );
        expenseRepository.save(expense);


        idem.setStatus(IdempotencyKey.Status.COMPLETED);
        idem.setResource(expense);
        idempotencyKeyRepository.save(idem);
    }


    public List<Expense> getCurrentUserExpensesList(Integer year, Integer month) {
        User user = this.userService.getCurrentLoggedUser();
        if ((year == null) && (month == null)) {
            return this.expenseRepository.findAllByUserIdAndDeletedFalse    (user.getId());
        } else {
            return null; // Aca va a ir la logica para setear el otro filtro
        }
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
