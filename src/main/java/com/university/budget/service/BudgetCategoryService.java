package com.university.budget.service;

import com.university.budget.domain.BudgetCategoryDTO;
import java.util.List;
import java.util.UUID;

public interface BudgetCategoryService {
    List<BudgetCategoryDTO> findAllCategories(UUID userId);
    BudgetCategoryDTO createCategory(UUID userId, BudgetCategoryDTO categoryDTO);
    void deleteCategory(UUID id);
}
