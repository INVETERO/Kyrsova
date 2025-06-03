package com.university.budget.service;

import com.university.budget.domain.GoalDTO;
import java.util.List;
import java.util.UUID;

public interface GoalService {
    List<GoalDTO> findAllGoals(UUID userId);
    GoalDTO createGoal(UUID userId, GoalDTO goalDTO);
    void deleteGoal(UUID id);
}
