package com.university.budget.service;

import com.university.budget.domain.IncomeDTO;
import java.util.List;
import java.util.UUID;

public interface IncomeService {
    List<IncomeDTO> findAllIncomes(UUID userId);
    IncomeDTO createIncome(UUID userId, IncomeDTO incomeDTO);
    void deleteIncome(UUID id);
}
