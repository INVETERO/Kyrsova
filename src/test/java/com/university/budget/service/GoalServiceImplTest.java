package com.university.budget.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.budget.domain.GoalDTO;
import com.university.budget.entity.GoalEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.repository.GoalRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;
    @Mock
    private UserRepository userRepository;

    private GoalServiceImpl goalService;

    private UUID userId;
    private UUID goalId;
    private GoalEntity goalEntity;
    private GoalDTO goalDTO;

    @BeforeEach
    void setUp() {
        goalService = new GoalServiceImpl(goalRepository, userRepository);
        userId = UUID.randomUUID();
        goalId = UUID.randomUUID();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username("testuser")
                .build();

        goalEntity = GoalEntity.builder()
                .id(goalId)
                .user(userEntity)
                .description("Save for vacation")
                .targetAmount(BigDecimal.valueOf(5000))
                .deadline(LocalDate.of(2026, 6, 1))
                .build();

        goalDTO = GoalDTO.builder()
                .description("Save for vacation")
                .targetAmount(BigDecimal.valueOf(5000))
                .deadline(LocalDate.of(2026, 6, 1))
                .build();
    }

    @Test
    void testFindAllGoals() {
        when(goalRepository.findByUserId(userId)).thenReturn(List.of(goalEntity));

        List<GoalDTO> result = goalService.findAllGoals(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Save for vacation", result.get(0).getDescription());
        verify(goalRepository).findByUserId(userId);
    }

    @Test
    void testCreateGoal() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(goalRepository.save(any(GoalEntity.class))).thenReturn(goalEntity);

        GoalDTO result = goalService.createGoal(userId, goalDTO);

        assertNotNull(result);
        assertEquals("Save for vacation", result.getDescription());
        verify(goalRepository).save(any(GoalEntity.class));
    }

    @Test
    void testDeleteGoal() {
        when(goalRepository.existsById(goalId)).thenReturn(true);
        goalService.deleteGoal(goalId);

        verify(goalRepository).deleteById(goalId);
    }
}
