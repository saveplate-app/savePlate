package com.saveplate.api.service;

import com.saveplate.api.dto.auth.AuthResponse;
import com.saveplate.api.dto.auth.LoginRequest;
import com.saveplate.api.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
