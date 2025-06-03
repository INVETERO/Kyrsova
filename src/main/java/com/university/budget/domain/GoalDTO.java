package com.university.budget.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class GoalDTO {

    UUID id;

    @NotNull(message = "Description must be provided")
    String description;

    @NotNull(message = "Target amount must be provided")
    BigDecimal targetAmount;

    LocalDate deadline;
}
