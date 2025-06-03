package com.university.budget.service.impl;

import com.university.budget.domain.GoalDTO;
import com.university.budget.entity.GoalEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.exception.GoalNotFoundException;
import com.university.budget.exception.UserNotFoundException;
import com.university.budget.repository.GoalRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Override
    public List<GoalDTO> findAllGoals(UUID userId) {
        return goalRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GoalDTO createGoal(UUID userId, GoalDTO goalDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new GoalNotFoundException("Goal not found with id: " + userId));

        GoalEntity goal = GoalEntity.builder()
                .user(user)
                .description(goalDTO.getDescription())
                .targetAmount(goalDTO.getTargetAmount())
                .deadline(goalDTO.getDeadline())
                .build();
        return mapToDTO(goalRepository.save(goal));
    }

    @Override
    public void deleteGoal(UUID id) {
        if (!goalRepository.existsById(id)) {
            throw new GoalNotFoundException("Goal not found with id: " + id);
        }
        goalRepository.deleteById(id);
    }

    private GoalDTO mapToDTO(GoalEntity goal) {
        return GoalDTO.builder()
                .id(goal.getId())
                .description(goal.getDescription())
                .targetAmount(goal.getTargetAmount())
                .deadline(goal.getDeadline())
                .build();
    }
}
