package com.pocketpipo.pocketpipo.repository;

import org.springframework.stereotype.Repository;

import com.pocketpipo.pocketpipo.entity.Budget;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    public Budget findByIdAndUserId(long id, long userId);
    
}
