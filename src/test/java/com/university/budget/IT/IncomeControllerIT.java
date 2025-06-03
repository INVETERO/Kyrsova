package com.university.budget.IT;

import com.university.budget.config.TestDataInitializer;
import com.university.budget.domain.IncomeDTO;
import com.university.budget.entity.IncomeEntity;
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
public class IncomeControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestDataInitializer dataInitializer;

    private String userToken;
    private UUID userId;
    private UUID incomeId;

    @BeforeEach
    void setup() {
        TestDataInitializer.TestData testData = dataInitializer.initTestData();
        userId = testData.userId();
        userToken = testData.userToken();
        incomeId = testData.income1Id();
    }

    @Test
    void shouldRetrieveAllIncomes() {
        webTestClient.get()
                .uri("/api/v1/incomes/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(IncomeDTO.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(2, response.getResponseBody().size());
                });
    }

    @Test
    void shouldCreateNewIncome() {
        IncomeEntity newIncome = IncomeEntity.builder()
                .source("Investment")
                .amount(BigDecimal.valueOf(500))
                .date(LocalDateTime.now())
                .build();

        webTestClient.post()
                .uri("/api/v1/incomes/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(newIncome)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IncomeDTO.class)
                .consumeWith(response -> {
                    IncomeDTO income = response.getResponseBody();
                    assertNotNull(income);
                    assertEquals("Investment", income.getSource());
                });
    }

    @Test
    void shouldDeleteIncome() {
        webTestClient.delete()
                .uri("/api/v1/incomes/{id}", incomeId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isNoContent();
    }
}
