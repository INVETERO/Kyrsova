package com.university.budget.service.impl;

import com.university.budget.domain.auth.LoginDTO;
import com.university.budget.domain.auth.RegisterDTO;
import com.university.budget.domain.UserDTO;
import com.university.budget.domain.enums.Role;
import com.university.budget.repository.UserRepository;
import com.university.budget.entity.UserEntity;
import com.university.budget.exception.AuthenticationException;
import com.university.budget.exception.UserAlreadyExistsException;
import com.university.budget.security.jwt.JwtProvider;
import com.university.budget.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(RegisterDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        UserEntity user = userRepository.save(UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .totalBudget(request.getTotalAmount())
                .role(Role.USER)
                .build());

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    @Override
    public UserDTO loginUser(LoginDTO request) {
        Optional<UserEntity> userOpt = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));
        UserEntity user = userOpt.orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            System.out.println("Password does not match for email: " + request.getEmail());
            throw new AuthenticationException("Invalid email or password");
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    public String generateToken(UserDTO user) {
        Role role = userRepository.findByEmail(user.getEmail()).getRole();
        return jwtProvider.createToken(user.getEmail(), role);
    }
}
