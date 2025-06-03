package com.university.budget.IT;

import com.university.budget.config.TestDataInitializer;
import com.university.budget.domain.BudgetCategoryDTO;
import com.university.budget.entity.BudgetCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BudgetCategoryControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestDataInitializer dataInitializer;

    private String userToken;
    private UUID userId;
    private UUID categoryId;

    @BeforeEach
    void setup() {
        TestDataInitializer.TestData testData = dataInitializer.initTestData();
        userId = testData.userId();
        userToken = testData.userToken();
        categoryId = testData.categoryFoodId();
    }

    @Test
    void shouldRetrieveAllCategories() {
        webTestClient.get()
                .uri("/api/v1/categories/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BudgetCategoryDTO.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(2, response.getResponseBody().size());
                });
    }

    @Test
    void shouldCreateNewCategory() {
        BudgetCategoryEntity newCategory = BudgetCategoryEntity.builder()
                .name("Utilities")
                .allocatedAmount(BigDecimal.valueOf(250))
                .build();

        webTestClient.post()
                .uri("/api/v1/categories/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(newCategory)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BudgetCategoryDTO.class)
                .consumeWith(response -> {
                    BudgetCategoryDTO category = response.getResponseBody();
                    assertNotNull(category);
                    assertEquals("Utilities", category.getName());
                });
    }

    @Test
    void shouldDeleteCategory() {
        webTestClient.delete()
                .uri("/api/v1/categories/{id}", categoryId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isNoContent();
    }
}
