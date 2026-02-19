package com.budgettracker.api.controller;

import com.budgettracker.api.dto.BudgetRequest;
import com.budgettracker.api.dto.BudgetResponse;
import com.budgettracker.api.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@Tag(name = "Budget", description = "Budget management API")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @Operation(summary = "Create a new budget", description = "Creates a new monthly budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Budget created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Budget for this month already exists")
    })
    public ResponseEntity<BudgetResponse> createBudget(@Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.createBudget(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get budget by ID", description = "Retrieves a budget by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget found"),
            @ApiResponse(responseCode = "404", description = "Budget not found")
    })
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Long id) {
        BudgetResponse response = budgetService.getBudgetById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all budgets", description = "Retrieves all budgets")
    @ApiResponse(responseCode = "200", description = "Budgets retrieved successfully")
    public ResponseEntity<List<BudgetResponse>> getAllBudgets() {
        List<BudgetResponse> response = budgetService.getAllBudgets();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update budget", description = "Updates an existing budget")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Budget not found"),
            @ApiResponse(responseCode = "409", description = "Budget for this month already exists")
    })
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete budget", description = "Deletes a budget by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Budget deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Budget not found")
    })
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
