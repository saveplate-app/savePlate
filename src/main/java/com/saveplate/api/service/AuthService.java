package com.saveplate.api.service;

import com.saveplate.api.dto.auth.*;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);

}
