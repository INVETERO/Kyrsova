package com.university.budget.service.impl;

import com.university.budget.domain.IncomeDTO;
import com.university.budget.entity.IncomeEntity;
import com.university.budget.entity.UserEntity;
import com.university.budget.exception.UserNotFoundException;
import com.university.budget.repository.IncomeRepository;
import com.university.budget.repository.UserRepository;
import com.university.budget.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Override
    public List<IncomeDTO> findAllIncomes(UUID userId) {
        return incomeRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IncomeDTO createIncome(UUID userId, IncomeDTO incomeDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        IncomeEntity income = IncomeEntity.builder()
                .user(user)
                .source(incomeDTO.getSource())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .build();
        return mapToDTO(incomeRepository.save(income));
    }

    @Override
    public void deleteIncome(UUID id) {
        if (!incomeRepository.existsById(id)) {
            throw new IllegalArgumentException("Income not found with id: " + id);
        }
        incomeRepository.deleteById(id);
    }

    private IncomeDTO mapToDTO(IncomeEntity income) {
        return IncomeDTO.builder()
                .id(income.getId())
                .source(income.getSource())
                .amount(income.getAmount())
                .date(income.getDate())
                .build();
    }
}
