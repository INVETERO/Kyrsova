package com.university.budget.repository;

import com.university.budget.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, UUID> {
    List<GoalEntity> findByUserId(UUID userId);
}
