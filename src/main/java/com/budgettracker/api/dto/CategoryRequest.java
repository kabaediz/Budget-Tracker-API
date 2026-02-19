package com.budgettracker.api.dto;

import com.budgettracker.api.model.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Type is required")
    private Category.CategoryType type;

    @NotNull(message = "Planned amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Planned amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Invalid amount format")
    private BigDecimal plannedAmount;

    @NotNull(message = "Budget ID is required")
    private Long budgetId;
}
