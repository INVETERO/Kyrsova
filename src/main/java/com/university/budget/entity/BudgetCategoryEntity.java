package com.university.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "budget_categories")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "allocated_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal allocatedAmount;
}
