package com.university.budget.repository;

import com.university.budget.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {
    List<ExpenseEntity> findByUserId(UUID userId);
}
