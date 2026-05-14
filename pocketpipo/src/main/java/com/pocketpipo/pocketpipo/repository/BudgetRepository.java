package com.pocketpipo.pocketpipo.repository;

import org.springframework.stereotype.Repository;

import com.pocketpipo.pocketpipo.entity.Budget;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    public Budget findByIdAndUserIdAndDeletedFalse(long id, long userId);

    public List<Budget> findByUserIdAndDeletedFalse(long userId);

    public Budget findByUserIdAndDeletedFalseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        long userId,
        LocalDate date,
        LocalDate sameDate
    );
    
}
