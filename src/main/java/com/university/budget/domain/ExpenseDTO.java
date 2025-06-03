package com.university.budget.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ExpenseDTO {

    UUID id;

    @NotNull(message = "Category must be provided")
    String category;

    @NotNull(message = "Amount must be provided")
    BigDecimal amount;

    LocalDateTime date;
}
