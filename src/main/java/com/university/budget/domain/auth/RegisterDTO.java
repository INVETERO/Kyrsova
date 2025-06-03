package com.university.budget.domain.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterDTO {
    @NotNull(message = "Email must be provided")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotNull(message = "TotalAmount must be provided")
    private BigDecimal totalAmount;

    @NotBlank(message = "Password must not be empty")
    private String password;
}

