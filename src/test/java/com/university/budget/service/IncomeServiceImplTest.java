package com.university.budget.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.budget.domain.IncomeDTO;
import com.university.budget.entity.IncomeEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.repository.IncomeRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.impl.IncomeServiceImpl;
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
public class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private UserRepository userRepository;

    private IncomeServiceImpl incomeService;

    private UUID userId;
    private UUID incomeId;
    private IncomeEntity incomeEntity;
    private IncomeDTO incomeDTO;

    @BeforeEach
    void setUp() {
        incomeService = new IncomeServiceImpl(incomeRepository, userRepository);
        userId = UUID.randomUUID();
        incomeId = UUID.randomUUID();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username("testuser")
                .build();

        incomeEntity = IncomeEntity.builder()
                .id(incomeId)
                .user(userEntity)
                .source("Salary")
                .amount(BigDecimal.valueOf(2000))
                .date(LocalDateTime.now())
                .build();

        incomeDTO = IncomeDTO.builder()
                .source("Salary")
                .amount(BigDecimal.valueOf(2000))
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindAllIncomes() {
        when(incomeRepository.findByUserId(userId)).thenReturn(List.of(incomeEntity));

        List<IncomeDTO> result = incomeService.findAllIncomes(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(incomeRepository).findByUserId(userId);
    }

    @Test
    void testCreateIncome() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(incomeRepository.save(any(IncomeEntity.class))).thenReturn(incomeEntity);

        IncomeDTO result = incomeService.createIncome(userId, incomeDTO);

        assertNotNull(result);
        assertEquals("Salary", result.getSource());
        verify(incomeRepository).save(any(IncomeEntity.class));
    }

    @Test
    void testDeleteIncome() {
        when(incomeRepository.existsById(incomeId)).thenReturn(true);
        incomeService.deleteIncome(incomeId);

        verify(incomeRepository).deleteById(incomeId);
    }
}
