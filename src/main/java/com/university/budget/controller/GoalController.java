package com.university.budget.controller;

import com.university.budget.domain.GoalDTO;
import com.university.budget.service.GoalService;
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
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "APIs for managing financial goals")
public class GoalController {

    private final GoalService goalService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get all financial goals for a user")
    public ResponseEntity<List<GoalDTO>> getGoals(@PathVariable UUID userId) {
        return ResponseEntity.ok(goalService.findAllGoals(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/{userId}")
    @Operation(summary = "Create a new financial goal")
    public ResponseEntity<GoalDTO> createGoal(@PathVariable UUID userId, @Valid @RequestBody GoalDTO goalDTO) {
        return new ResponseEntity<>(goalService.createGoal(userId, goalDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a financial goal")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
