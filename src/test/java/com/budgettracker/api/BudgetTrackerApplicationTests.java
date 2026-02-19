package com.budgettracker.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
class BudgetTrackerApplicationTests {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
    }
}
