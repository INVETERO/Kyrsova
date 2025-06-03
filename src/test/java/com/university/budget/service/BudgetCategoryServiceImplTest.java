package com.university.budget.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.university.budget.domain.BudgetCategoryDTO;
import com.university.budget.entity.BudgetCategoryEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.repository.BudgetCategoryRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.impl.BudgetCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BudgetCategoryServiceImplTest {

    @Mock
    private BudgetCategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;

    private BudgetCategoryServiceImpl categoryService;

    private UUID userId;
    private UUID categoryId;
    private BudgetCategoryEntity categoryEntity;
    private BudgetCategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryService = new BudgetCategoryServiceImpl(categoryRepository, userRepository);
        userId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username("testuser")
                .build();

        categoryEntity = BudgetCategoryEntity.builder()
                .id(categoryId)
                .user(userEntity)
                .name("Entertainment")
                .allocatedAmount(BigDecimal.valueOf(300))
                .build();

        categoryDTO = BudgetCategoryDTO.builder()
                .name("Entertainment")
                .allocatedAmount(BigDecimal.valueOf(300))
                .build();
    }

    @Test
    void testFindAllCategories() {
        when(categoryRepository.findByUserId(userId)).thenReturn(List.of(categoryEntity));

        List<BudgetCategoryDTO> result = categoryService.findAllCategories(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Entertainment", result.get(0).getName());
        verify(categoryRepository).findByUserId(userId);
    }

    @Test
    void testCreateCategory() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(categoryRepository.save(any(BudgetCategoryEntity.class))).thenReturn(categoryEntity);

        BudgetCategoryDTO result = categoryService.createCategory(userId, categoryDTO);

        assertNotNull(result);
        assertEquals("Entertainment", result.getName());
        verify(categoryRepository).save(any(BudgetCategoryEntity.class));
    }

    @Test
    void testDeleteCategory() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }
}
