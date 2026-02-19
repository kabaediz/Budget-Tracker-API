package com.budgettracker.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private Long categoryId;
    private String categoryName;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
