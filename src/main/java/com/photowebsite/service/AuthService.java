package com.photowebsite.service;

import com.photowebsite.dto.request.LoginRequest;
import com.photowebsite.dto.request.RegisterRequest;
import com.photowebsite.dto.response.AuthResponse;
import com.photowebsite.dto.response.TokenResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest loginRequest);
    void logout(String token);
    TokenResponse refreshToken(String token);
    void verifyEmail(String token);
}