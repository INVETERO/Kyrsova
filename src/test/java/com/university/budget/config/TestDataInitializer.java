package com.university.budget.config;

import com.university.budget.domain.enums.Role;
import com.university.budget.entity.*;
import com.university.budget.repository.*;
import com.university.budget.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataInitializer {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final GoalRepository goalRepository;

    public TestData initTestData() {
        goalRepository.deleteAll();
        budgetCategoryRepository.deleteAll();
        incomeRepository.deleteAll();
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity admin = userRepository.save(UserEntity.builder()
                .email("admin@budget.com")
                .passwordHash("securepassword")
                .username("BudgetAdmin")
                .role(Role.ADMIN)
                .totalBudget(BigDecimal.valueOf(10000))
                .build());

        String adminToken = jwtProvider.createToken(admin.getEmail(), admin.getRole());

        UserEntity user = userRepository.save(UserEntity.builder()
                .email("user@budget.com")
                .passwordHash("securepassword")
                .username("BudgetUser")
                .role(Role.USER)
                .totalBudget(BigDecimal.valueOf(5000))
                .build());

        String userToken = jwtProvider.createToken(user.getEmail(), user.getRole());

        BudgetCategoryEntity categoryFood = budgetCategoryRepository.save(BudgetCategoryEntity.builder()
                .user(user)
                .name("Food")
                .allocatedAmount(BigDecimal.valueOf(600))
                .build());

        BudgetCategoryEntity categoryRent = budgetCategoryRepository.save(BudgetCategoryEntity.builder()
                .user(user)
                .name("Rent")
                .allocatedAmount(BigDecimal.valueOf(1200))
                .build());

        ExpenseEntity expense1 = expenseRepository.save(ExpenseEntity.builder()
                .user(user)
                .category("Food")
                .amount(BigDecimal.valueOf(50))
                .date(LocalDateTime.now())
                .build());

        ExpenseEntity expense2 = expenseRepository.save(ExpenseEntity.builder()
                .user(user)
                .category("Transport")
                .amount(BigDecimal.valueOf(30))
                .date(LocalDateTime.now())
                .build());

        IncomeEntity income1 = incomeRepository.save(IncomeEntity.builder()
                .user(user)
                .source("Salary")
                .amount(BigDecimal.valueOf(2500))
                .date(LocalDateTime.now())
                .build());

        IncomeEntity income2 = incomeRepository.save(IncomeEntity.builder()
                .user(user)
                .source("Freelance")
                .amount(BigDecimal.valueOf(500))
                .date(LocalDateTime.now())
                .build());

        GoalEntity goal1 = goalRepository.save(GoalEntity.builder()
                .user(user)
                .description("Save for vacation")
                .targetAmount(BigDecimal.valueOf(2000))
                .deadline(LocalDate.of(2026, 6, 1))
                .build());

        return new TestData(admin.getId(), user.getId(), categoryFood.getId(), categoryRent.getId(),
                expense1.getId(), expense2.getId(), income1.getId(), income2.getId(), goal1.getId(),
                adminToken, userToken);
    }

    public record TestData(UUID adminId, UUID userId, UUID categoryFoodId, UUID categoryRentId,
                           UUID expense1Id, UUID expense2Id, UUID income1Id, UUID income2Id,
                           UUID goal1Id, String adminToken, String userToken) {}
}
