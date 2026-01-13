package com.pocketpipo.pocketpipo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketpipo.pocketpipo.dto.CreateExpenseRequestDTO;
import com.pocketpipo.pocketpipo.dto.ExpenseResponseDTO;
import com.pocketpipo.pocketpipo.service.ExpenseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/expenses")
public ResponseEntity<Void> createExpense(
        @RequestBody CreateExpenseRequestDTO createExpenseDTO,
        @RequestHeader("Idempotency-Key") String idempotencyKey,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month
) {
    this.expenseService.createExpense(createExpenseDTO, idempotencyKey);
    return ResponseEntity.status(HttpStatus.CREATED).build();
}

    
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month
    ) {
        List<ExpenseResponseDTO> expenses =
            expenseService.getCurrentUserExpensesList(year, month)
                    .stream()
                    .map(expenseService::toExpenseResponseDTO)
                    .toList();

        return ResponseEntity.ok(expenses);
        }


   @DeleteMapping("/{expenseId}") 
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        this.expenseService.deleteExpenseById(expenseId);
        return ResponseEntity.ok("The expense was succesfully deleted.");

    }
    

}
