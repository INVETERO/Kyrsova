package com.university.budget.controller;

import com.university.budget.domain.ExpenseDTO;
import com.university.budget.service.ExpenseService;
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
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "APIs for managing expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get all expenses for a user")
    public ResponseEntity<List<ExpenseDTO>> getExpenses(@PathVariable UUID userId) {
        return ResponseEntity.ok(expenseService.findAllExpenses(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/{userId}")
    @Operation(summary = "Create a new expense")
    public ResponseEntity<ExpenseDTO> createExpense(@PathVariable UUID userId, @Valid @RequestBody ExpenseDTO expenseDTO) {
        return new ResponseEntity<>(expenseService.createExpense(userId, expenseDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
