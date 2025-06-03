package com.university.budget.IT;

import com.university.budget.config.TestDataInitializer;
import com.university.budget.domain.GoalDTO;
import com.university.budget.entity.GoalEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GoalControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TestDataInitializer dataInitializer;

    private String userToken;
    private UUID userId;
    private UUID goalId;

    @BeforeEach
    void setup() {
        TestDataInitializer.TestData testData = dataInitializer.initTestData();
        userId = testData.userId();
        userToken = testData.userToken();
        goalId = testData.goal1Id();
    }

    @Test
    void shouldRetrieveAllGoals() {
        webTestClient.get()
                .uri("/api/v1/goals/{userId}", userId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GoalDTO.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(1, response.getResponseBody().size());
                });
    }

    @Test
    void shouldDeleteGoal() {
        webTestClient.delete()
                .uri("/api/v1/goals/{id}", goalId)
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isNoContent();
    }
}
