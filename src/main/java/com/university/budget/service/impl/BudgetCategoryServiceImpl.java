package com.university.budget.service.impl;

import com.university.budget.domain.BudgetCategoryDTO;
import com.university.budget.entity.BudgetCategoryEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.exception.BudgetCategoryNotFoundException;
import com.university.budget.exception.UserNotFoundException;
import com.university.budget.repository.BudgetCategoryRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.BudgetCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetCategoryServiceImpl implements BudgetCategoryService {

    private final BudgetCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<BudgetCategoryDTO> findAllCategories(UUID userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BudgetCategoryDTO createCategory(UUID userId, BudgetCategoryDTO categoryDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        BudgetCategoryEntity category = BudgetCategoryEntity.builder()
                .user(user)
                .name(categoryDTO.getName())
                .allocatedAmount(categoryDTO.getAllocatedAmount())
                .build();
        return mapToDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new BudgetCategoryNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private BudgetCategoryDTO mapToDTO(BudgetCategoryEntity category) {
        return BudgetCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .allocatedAmount(category.getAllocatedAmount())
                .build();
    }
}
