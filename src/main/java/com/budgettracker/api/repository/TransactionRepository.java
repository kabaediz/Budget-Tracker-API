package com.budgettracker.api.repository;

import com.budgettracker.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByCategoryId(Long categoryId);
    
    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
}
