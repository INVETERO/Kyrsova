package com.university.budget.exception;

public class BudgetCategoryNotFoundException extends RuntimeException {
    public BudgetCategoryNotFoundException(String message) {
        super(message);
    }
}
