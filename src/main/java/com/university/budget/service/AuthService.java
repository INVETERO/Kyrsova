package com.university.budget.service;

import com.university.budget.domain.auth.LoginDTO;
import com.university.budget.domain.auth.RegisterDTO;
import com.university.budget.domain.UserDTO;

public interface AuthService {
    UserDTO loginUser(LoginDTO request);

    UserDTO registerUser(RegisterDTO request);
    String generateToken(UserDTO user);
}
