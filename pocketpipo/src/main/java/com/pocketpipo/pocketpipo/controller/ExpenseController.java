package com.pocketpipo.pocketpipo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.service.ExpenseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService taskService;

    public ExpenseController(ExpenseService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/expenses")
    public ResponseEntity<Void> createExpense(@RequestBody CreateExpenseRequestDTO createExpenseDTO) {
        this.taskService.createExpense(createExpenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
}
