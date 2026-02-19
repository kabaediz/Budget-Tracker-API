package com.budgettracker.api.service;

import com.budgettracker.api.dto.CategoryRequest;
import com.budgettracker.api.dto.CategoryResponse;
import com.budgettracker.api.exception.ResourceNotFoundException;
import com.budgettracker.api.model.Budget;
import com.budgettracker.api.model.Category;
import com.budgettracker.api.repository.BudgetRepository;
import com.budgettracker.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;

    public CategoryResponse createCategory(CategoryRequest request) {
        Budget budget = budgetRepository.findById(request.getBudgetId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget", request.getBudgetId()));

        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .plannedAmount(request.getPlannedAmount())
                .budget(budget)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        return mapToResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByBudgetId(Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new ResourceNotFoundException("Budget", budgetId);
        }
        return categoryRepository.findByBudgetId(budgetId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        Budget budget = budgetRepository.findById(request.getBudgetId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget", request.getBudgetId()));

        category.setName(request.getName());
        category.setType(request.getType());
        category.setPlannedAmount(request.getPlannedAmount());
        category.setBudget(budget);

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponse mapToResponse(Category category) {
        BigDecimal actualAmount = category.getTransactions().stream()
                .map(transaction -> transaction.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = category.getPlannedAmount().subtract(actualAmount);

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType().name())
                .plannedAmount(category.getPlannedAmount())
                .actualAmount(actualAmount)
                .remaining(remaining)
                .budgetId(category.getBudget().getId())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
