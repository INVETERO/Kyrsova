package com.university.budget.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.budget.domain.ExpenseDTO;
import com.university.budget.entity.ExpenseEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.repository.ExpenseRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private UserRepository userRepository;

    private ExpenseServiceImpl expenseService;

    private UUID userId;
    private UUID expenseId;
    private ExpenseEntity expenseEntity;
    private ExpenseDTO expenseDTO;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseServiceImpl(expenseRepository, userRepository);
        userId = UUID.randomUUID();
        expenseId = UUID.randomUUID();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username("testuser")
                .build();

        expenseEntity = ExpenseEntity.builder()
                .id(expenseId)
                .user(userEntity)
                .category("Food")
                .amount(BigDecimal.valueOf(50))
                .date(LocalDateTime.now())
                .build();

        expenseDTO = ExpenseDTO.builder()
                .category("Food")
                .amount(BigDecimal.valueOf(50))
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindAllExpenses() {
        when(expenseRepository.findByUserId(userId)).thenReturn(List.of(expenseEntity));

        List<ExpenseDTO> result = expenseService.findAllExpenses(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(expenseRepository).findByUserId(userId);
    }

    @Test
    void testCreateExpense() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(expenseRepository.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);

        ExpenseDTO result = expenseService.createExpense(userId, expenseDTO);

        assertNotNull(result);
        assertEquals("Food", result.getCategory());
        verify(expenseRepository).save(any(ExpenseEntity.class));
    }

    @Test
    void testDeleteExpense() {
        when(expenseRepository.existsById(expenseId)).thenReturn(true);
        expenseService.deleteExpense(expenseId);

        verify(expenseRepository).deleteById(expenseId);
    }
}
