package com.budgettracker.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String type;
    private BigDecimal plannedAmount;
    private BigDecimal actualAmount;
    private BigDecimal remaining;
    private Long budgetId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
