package com.university.budget.controller;

import com.university.budget.domain.BudgetCategoryDTO;
import com.university.budget.service.BudgetCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Budget Categories", description = "APIs for managing budget categories")
public class BudgetCategoryController {

    private final BudgetCategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get all budget categories for a user")
    public ResponseEntity<List<BudgetCategoryDTO>> getCategories(@PathVariable UUID userId) {
        return ResponseEntity.ok(categoryService.findAllCategories(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/{userId}")
    @Operation(summary = "Create a new budget category")
    public ResponseEntity<BudgetCategoryDTO> createCategory(@PathVariable UUID userId, @Valid @RequestBody BudgetCategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(userId, categoryDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a budget category")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
