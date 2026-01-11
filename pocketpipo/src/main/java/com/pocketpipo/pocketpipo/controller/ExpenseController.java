package com.pocketpipo.pocketpipo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.dto.ExpenseResponseDTO;
import com.pocketpipo.pocketpipo.service.ExpenseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/expenses")
    public ResponseEntity<Void> createExpense(@RequestBody CreateExpenseRequestDTO createExpenseDTO) {
        this.expenseService.createExpense(createExpenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses() {
        List<ExpenseResponseDTO> expenses =
            expenseService.getCurrentUserExpensesList()
                    .stream()
                    .map(expenseService::toExpenseResponseDTO)
                    .toList();

        return ResponseEntity.ok(expenses);
        }
   
}
