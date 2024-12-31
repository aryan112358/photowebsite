package com.photowebsite.service.impl;

import com.photowebsite.entity.User;
import com.photowebsite.dto.request.RegisterRequest;
import com.photowebsite.dto.response.AuthResponse;
import com.photowebsite.dto.response.TokenResponse;
import com.photowebsite.dto.request.LoginRequest;
import com.photowebsite.service.AuthService;
import com.photowebsite.exception.AuthenticationException;
import com.photowebsite.repository.UserRepository;
import com.photowebsite.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import com.photowebsite.entity.Role;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        String token = tokenProvider.generateToken(user);
        return new AuthResponse(token, "Bearer");
    }

    @Override
    public void logout(String token) {
        // Add token to blacklist or invalidate in your preferred way
        tokenProvider.invalidateToken(token);
    }

    @Override
    public TokenResponse refreshToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new AuthenticationException("Invalid token");
        }
        
        Long userId = tokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthenticationException("User not found"));

        String newToken = tokenProvider.generateToken(user);
        return new TokenResponse(newToken);
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Check if user exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Collections.singleton(Role.ROLE_USER))
                .enabled(true)
                .build();

        user = userRepository.save(user);

        // Generate token
        String token = tokenProvider.generateToken(user);
        
        // Return auth response with token
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .build();
    }

    @Override
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new AuthenticationException("Invalid verification token"));
        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }
} 