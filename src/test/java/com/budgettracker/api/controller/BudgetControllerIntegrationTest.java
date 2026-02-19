package com.budgettracker.api.controller;

import com.budgettracker.api.dto.BudgetRequest;
import com.budgettracker.api.dto.BudgetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBudget_WithValidData_ShouldReturnCreated() throws Exception {
        BudgetRequest request = BudgetRequest.builder()
                .name("January Budget")
                .description("Budget for January 2024")
                .year(2024)
                .month(1)
                .build();

        mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("January Budget")))
                .andExpect(jsonPath("$.year", is(2024)))
                .andExpect(jsonPath("$.month", is(1)));
    }

    @Test
    void createBudget_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        BudgetRequest request = BudgetRequest.builder()
                .name("JB") // Too short
                .year(2024)
                .month(13) // Invalid month
                .build();

        mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Failed")))
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
    }

    @Test
    void getBudget_WhenExists_ShouldReturnBudget() throws Exception {
        // First create a budget
        BudgetRequest request = BudgetRequest.builder()
                .name("Test Budget")
                .year(2024)
                .month(2)
                .build();

        String response = mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BudgetResponse budgetResponse = objectMapper.readValue(response, BudgetResponse.class);

        // Then retrieve it
        mockMvc.perform(get("/api/v1/budgets/{id}", budgetResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(budgetResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Budget")));
    }

    @Test
    void getBudget_WhenNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/budgets/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Resource Not Found")));
    }

    @Test
    void getAllBudgets_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/v1/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void updateBudget_WithValidData_ShouldReturnUpdated() throws Exception {
        // First create a budget
        BudgetRequest createRequest = BudgetRequest.builder()
                .name("Original Name")
                .year(2024)
                .month(3)
                .build();

        String createResponse = mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BudgetResponse budgetResponse = objectMapper.readValue(createResponse, BudgetResponse.class);

        // Then update it
        BudgetRequest updateRequest = BudgetRequest.builder()
                .name("Updated Name")
                .year(2024)
                .month(3)
                .build();

        mockMvc.perform(put("/api/v1/budgets/{id}", budgetResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    void deleteBudget_WhenExists_ShouldReturnNoContent() throws Exception {
        // First create a budget
        BudgetRequest request = BudgetRequest.builder()
                .name("To Delete")
                .year(2024)
                .month(4)
                .build();

        String response = mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BudgetResponse budgetResponse = objectMapper.readValue(response, BudgetResponse.class);

        // Then delete it
        mockMvc.perform(delete("/api/v1/budgets/{id}", budgetResponse.getId()))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/budgets/{id}", budgetResponse.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBudget_WithDuplicateYearMonth_ShouldReturnConflict() throws Exception {
        BudgetRequest request = BudgetRequest.builder()
                .name("First Budget")
                .year(2024)
                .month(5)
                .build();

        // Create first budget
        mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Try to create duplicate
        BudgetRequest duplicateRequest = BudgetRequest.builder()
                .name("Duplicate Budget")
                .year(2024)
                .month(5)
                .build();

        mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title", is("Duplicate Resource")));
    }
}
