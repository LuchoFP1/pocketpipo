package com.pocketpipo.pocketpipo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocketpipo.pocketpipo.dto.CreateBudgetRequestDTO;
import com.pocketpipo.pocketpipo.entity.Budget;
import com.pocketpipo.pocketpipo.service.BudgetService;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody CreateBudgetRequestDTO budget) {
        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    @PatchMapping("/{budgetId}") 
    public void updateEntity(@RequestBody CreateBudgetRequestDTO budget, @PathVariable long budgetId) {
        this.budgetService.updateBudget(budget, budgetId);
    }


    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudgetById(@PathVariable long budgetId) {
        this.budgetService.deleteBudget(budgetId);
        return ResponseEntity.ok("The budget was succesfully deleted.");
    }
}
