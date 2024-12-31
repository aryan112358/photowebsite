package com.photowebsite.service;

public interface EmailService {
    void sendVerificationEmail(String email, String token);
} 