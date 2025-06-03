package com.university.budget.IT;

import com.university.budget.config.TestDataInitializer;
import com.university.budget.domain.ExpenseDTO;
import com.university.budget.entity.ExpenseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpenseControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestDataInitializer dataInitializer;

    private String userToken;
    private UUID userId;
    private UUID expenseId;

    @BeforeEach
    void setup() {
        TestDataInitializer.TestData testData = dataInitializer.initTestData();
        userId = testData.userId();
        userToken = testData.userToken();
        expenseId = testData.expense1Id();
    }

    @Test
    void shouldRetrieveAllExpenses() {
        webTestClient.get()
                .uri("/api/v1/expenses/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExpenseDTO.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(2, response.getResponseBody().size());
                });
    }

    @Test
    void shouldCreateNewExpense() {
        ExpenseEntity newExpense = ExpenseEntity.builder()
                .category("Entertainment")
                .amount(BigDecimal.valueOf(100))
                .date(LocalDateTime.now())
                .build();

        webTestClient.post()
                .uri("/api/v1/expenses/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(newExpense)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ExpenseDTO.class)
                .consumeWith(response -> {
                    ExpenseDTO expense = response.getResponseBody();
                    assertNotNull(expense);
                    assertEquals("Entertainment", expense.getCategory());
                });
    }

    @Test
    void shouldDeleteExpense() {
        webTestClient.delete()
                .uri("/api/v1/expenses/{id}", expenseId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isNoContent();
    }
}
