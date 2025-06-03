package com.university.budget.service;

import com.university.budget.domain.ExpenseDTO;
import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    List<ExpenseDTO> findAllExpenses(UUID userId);
    ExpenseDTO createExpense(UUID userId, ExpenseDTO expenseDTO);
    void deleteExpense(UUID id);
}
