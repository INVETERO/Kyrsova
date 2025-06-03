package com.university.budget.service.impl;

import com.university.budget.domain.ExpenseDTO;
import com.university.budget.entity.ExpenseEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.exception.UserNotFoundException;
import com.university.budget.repository.ExpenseRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public List<ExpenseDTO> findAllExpenses(UUID userId) {
        return expenseRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDTO createExpense(UUID userId, ExpenseDTO expenseDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        ExpenseEntity expense = ExpenseEntity.builder()
                .user(user)
                .category(expenseDTO.getCategory())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .build();
        return mapToDTO(expenseRepository.save(expense));
    }

    @Override
    public void deleteExpense(UUID id) {
        if (!expenseRepository.existsById(id)) {
            throw new IllegalArgumentException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    private ExpenseDTO mapToDTO(ExpenseEntity expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .build();
    }
}
