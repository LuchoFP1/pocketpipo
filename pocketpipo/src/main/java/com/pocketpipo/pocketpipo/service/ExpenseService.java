package com.pocketpipo.pocketpipo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.dto.ExpenseResponseDTO;
import com.pocketpipo.pocketpipo.entity.Budget;
import com.pocketpipo.pocketpipo.entity.Expense;
import com.pocketpipo.pocketpipo.entity.IdempotencyKey;
import com.pocketpipo.pocketpipo.entity.User;
import com.pocketpipo.pocketpipo.exception.ExpenseNotFoundException;
import com.pocketpipo.pocketpipo.exception.InvalidDateException;
import com.pocketpipo.pocketpipo.producer.BudgetEventProducer;
import com.pocketpipo.pocketpipo.repository.ExpenseRepository;
import com.pocketpipo.pocketpipo.repository.IdempotencyKeyRepository;


@Service
public class ExpenseService {
    private static final String OP_CREATE_EXPENSE = "CREATE_EXPENSE";
    private final ExpenseRepository expenseRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final UserService userService;
    private final BudgetEventProducer budgetEventProducer;
    private final BudgetService budgetService;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            IdempotencyKeyRepository idempotencyKeyRepository,
            UserService userService,
            BudgetEventProducer budgetEventProducer,
            BudgetService budgetService
    ) {
        this.expenseRepository = expenseRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.userService = userService;
        this.budgetEventProducer = budgetEventProducer;
        this.budgetService = budgetService;
    }


   @Transactional
    public void createExpense(CreateExpenseRequestDTO dto, String idempotencyKey) {
        User user = this.userService.getCurrentLoggedUser();
        boolean shouldPublishBudgetThresholdEvent = false;

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

        Budget budget = this.budgetService.findActiveBudgetForExpense(expense.getDate());
        if (budget != null) {
            expense.setBudget(budget);
            List<Expense> expenses = this.expenseRepository.findByUserIdAndBudgetId(user.getId(), budget.getId());
            BigDecimal totalAmount = expense.getAmount();
            for (Expense currentExpense : expenses) {
                totalAmount = totalAmount.add(currentExpense.getAmount());
            }
            BigDecimal thresholdAmount = budget.getMaxAmount()
            .multiply(BigDecimal.valueOf(75))
            .divide(BigDecimal.valueOf(100));
            if (totalAmount.compareTo(thresholdAmount) >= 0) {
                shouldPublishBudgetThresholdEvent = true;
            }
        }
        expense = expenseRepository.save(expense);
        
        if (shouldPublishBudgetThresholdEvent ) {
            budgetEventProducer.publishBudgetThresholdExceeded(
                user.getId(),
                budget.getId(),
                budget.getName(),
                expense.getId(),
                expense.getAmount(),
                previousTotal,
                newTotal,
                budget.getMaxAmount(),
                thresholdAmount,
                75,
                expense.getDate()
            );
        }


        idem.setStatus(IdempotencyKey.Status.COMPLETED);
        idem.setResource(expense);
        idempotencyKeyRepository.save(idem);

    }


    public List<Expense> getCurrentUserExpensesList(Integer month, Integer year) {
        this.validateMonthAndYear(month, year);
        User user = this.userService.getCurrentLoggedUser();
        if ((year == null) && (month == null)) {
            return this.expenseRepository.findAllByUserIdAndDeletedFalse(user.getId());
        } else {
            LocalDate startDate = LocalDate.of(year, 1, 1);
            LocalDate endDate = startDate.plusYears(1);;
            if ( month != null ) {
                startDate = LocalDate.of(year, month, 1);
                endDate = startDate.plusMonths(1);
            }
            return this.expenseRepository.findAllByUserIdAndDeletedFalseAndDateGreaterThanEqualAndDateLessThan(user.getId(), startDate, endDate);

        }
    }

    @Transactional
    public void deleteExpenseById(Long expenseId) {
        Expense expense = this.getExpenseByIdForCurrentUser(expenseId);
        expense.setDeleted(true);
        this.expenseRepository.save(expense);
    }


    public Expense getExpenseById(Long expenseId) {
        if (expenseId == null) {
            throw new RuntimeException("Error: Id is null");
        }
        return this.getExpenseByIdForCurrentUser(expenseId);
    }

    public void updateExpense(Long expenseId, CreateExpenseRequestDTO expenseRequestDTO) {
        Expense expense = this.getExpenseByIdForCurrentUser(expenseId);
        expense.setAmount(expenseRequestDTO.getAmount());
        expense.setDate(expenseRequestDTO.getDate());
        expense.setDescription(expenseRequestDTO.getDescription());
        this.expenseRepository.save(expense);
    }

    // Helpers

    private Expense getExpenseByIdForCurrentUser(Long expenseId) {
        User user = this.userService.getCurrentLoggedUser();
        Expense expense = this.expenseRepository.findByIdAndUserIdAndDeletedFalse(expenseId, user.getId());
        if (expense == null) {
            throw new ExpenseNotFoundException("The requested expense does not exists");
        }
        return expense;
    }

    public ExpenseResponseDTO toExpenseResponseDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(), 
                expense.getDescription(),
                expense.getAmount(), 
                expense.getDate(),
                expense.getDeleted()
        );
    }

    public void validateMonthAndYear(Integer month, Integer year) {
        if ((year == null) && (month != null) ) {
            throw new InvalidDateException("Error: The year value can't be null");
         }
            else {
                if ( year != null) {      
            if ((year > 2100) || (year < 2000)) {
                throw new InvalidDateException("Error: Incorrect year value");
            }
                }
         }
        if (month != null) {
            if ((month > 12) || (month < 1)) {
                throw new InvalidDateException("Error: Incorrect month value");
            }
        }
    }


}
