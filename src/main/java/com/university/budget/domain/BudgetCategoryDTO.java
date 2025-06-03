package com.university.budget.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class BudgetCategoryDTO {

    UUID id;

    @NotNull(message = "Category name must be provided")
    String name;

    @NotNull(message = "Allocated amount must be provided")
    BigDecimal allocatedAmount;
}
