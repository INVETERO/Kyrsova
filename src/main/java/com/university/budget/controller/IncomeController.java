package com.university.budget.controller;

import com.university.budget.domain.IncomeDTO;
import com.university.budget.service.IncomeService;
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
@RequestMapping("/api/v1/incomes")
@RequiredArgsConstructor
@Tag(name = "Incomes", description = "APIs for managing incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get all incomes for a user")
    public ResponseEntity<List<IncomeDTO>> getIncomes(@PathVariable UUID userId) {
        return ResponseEntity.ok(incomeService.findAllIncomes(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/{userId}")
    @Operation(summary = "Create a new income")
    public ResponseEntity<IncomeDTO> createIncome(@PathVariable UUID userId, @Valid @RequestBody IncomeDTO incomeDTO) {
        return new ResponseEntity<>(incomeService.createIncome(userId, incomeDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an income")
    public ResponseEntity<Void> deleteIncome(@PathVariable UUID id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
