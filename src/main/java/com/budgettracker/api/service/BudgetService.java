package com.budgettracker.api.service;

import com.budgettracker.api.dto.BudgetRequest;
import com.budgettracker.api.dto.BudgetResponse;
import com.budgettracker.api.exception.DuplicateResourceException;
import com.budgettracker.api.exception.ResourceNotFoundException;
import com.budgettracker.api.model.Budget;
import com.budgettracker.api.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetResponse createBudget(BudgetRequest request) {
        if (budgetRepository.existsByYearAndMonth(request.getYear(), request.getMonth())) {
            throw new DuplicateResourceException(
                    String.format("Budget for year %d and month %d already exists", 
                            request.getYear(), request.getMonth()));
        }

        Budget budget = Budget.builder()
                .name(request.getName())
                .description(request.getDescription())
                .year(request.getYear())
                .month(request.getMonth())
                .build();

        Budget savedBudget = budgetRepository.save(budget);
        return mapToResponse(savedBudget);
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", id));
        return mapToResponse(budget);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponse> getAllBudgets() {
        return budgetRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BudgetResponse updateBudget(Long id, BudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", id));

        // Check if updating to a different year/month that already exists
        if (!budget.getYear().equals(request.getYear()) || !budget.getMonth().equals(request.getMonth())) {
            if (budgetRepository.existsByYearAndMonth(request.getYear(), request.getMonth())) {
                throw new DuplicateResourceException(
                        String.format("Budget for year %d and month %d already exists", 
                                request.getYear(), request.getMonth()));
            }
        }

        budget.setName(request.getName());
        budget.setDescription(request.getDescription());
        budget.setYear(request.getYear());
        budget.setMonth(request.getMonth());

        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponse(updatedBudget);
    }

    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Budget", id);
        }
        budgetRepository.deleteById(id);
    }

    private BudgetResponse mapToResponse(Budget budget) {
        List<BudgetResponse.CategorySummary> categories = budget.getCategories().stream()
                .map(category -> BudgetResponse.CategorySummary.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType().name())
                        .build())
                .collect(Collectors.toList());

        return BudgetResponse.builder()
                .id(budget.getId())
                .name(budget.getName())
                .description(budget.getDescription())
                .year(budget.getYear())
                .month(budget.getMonth())
                .categories(categories)
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .build();
    }
}
