package com.budgettracker.api.service;

import com.budgettracker.api.dto.BudgetRequest;
import com.budgettracker.api.dto.BudgetResponse;
import com.budgettracker.api.exception.DuplicateResourceException;
import com.budgettracker.api.exception.ResourceNotFoundException;
import com.budgettracker.api.model.Budget;
import com.budgettracker.api.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    private BudgetRequest budgetRequest;
    private Budget budget;

    @BeforeEach
    void setUp() {
        budgetRequest = BudgetRequest.builder()
                .name("Test Budget")
                .description("Test Description")
                .year(2024)
                .month(1)
                .build();

        budget = Budget.builder()
                .id(1L)
                .name("Test Budget")
                .description("Test Description")
                .year(2024)
                .month(1)
                .build();
    }

    @Test
    void createBudget_WithValidData_ShouldReturnBudgetResponse() {
        when(budgetRepository.existsByYearAndMonth(2024, 1)).thenReturn(false);
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        BudgetResponse response = budgetService.createBudget(budgetRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Test Budget");
        assertThat(response.getYear()).isEqualTo(2024);
        assertThat(response.getMonth()).isEqualTo(1);
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void createBudget_WithDuplicateYearMonth_ShouldThrowException() {
        when(budgetRepository.existsByYearAndMonth(2024, 1)).thenReturn(true);

        assertThatThrownBy(() -> budgetService.createBudget(budgetRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Budget for year 2024 and month 1 already exists");

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    void getBudgetById_WhenExists_ShouldReturnBudget() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        BudgetResponse response = budgetService.getBudgetById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test Budget");
    }

    @Test
    void getBudgetById_WhenNotExists_ShouldThrowException() {
        when(budgetRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.getBudgetById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Budget with id 999 not found");
    }

    @Test
    void getAllBudgets_ShouldReturnList() {
        Budget budget2 = Budget.builder()
                .id(2L)
                .name("Budget 2")
                .year(2024)
                .month(2)
                .build();

        when(budgetRepository.findAll()).thenReturn(Arrays.asList(budget, budget2));

        List<BudgetResponse> responses = budgetService.getAllBudgets();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("Test Budget");
        assertThat(responses.get(1).getName()).isEqualTo("Budget 2");
    }

    @Test
    void updateBudget_WithValidData_ShouldReturnUpdatedBudget() {
        BudgetRequest updateRequest = BudgetRequest.builder()
                .name("Updated Budget")
                .year(2024)
                .month(1)
                .build();

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        BudgetResponse response = budgetService.updateBudget(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void deleteBudget_WhenExists_ShouldDeleteBudget() {
        when(budgetRepository.existsById(1L)).thenReturn(true);
        doNothing().when(budgetRepository).deleteById(1L);

        budgetService.deleteBudget(1L);

        verify(budgetRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBudget_WhenNotExists_ShouldThrowException() {
        when(budgetRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> budgetService.deleteBudget(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(budgetRepository, never()).deleteById(999L);
    }
}
