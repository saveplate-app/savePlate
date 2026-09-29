package com.saveplate.api.service.impl;

import com.saveplate.api.dto.auth.*;
import com.saveplate.api.entities.User;
import com.saveplate.api.exceptions.EmailAlreadyExistsException;
import com.saveplate.api.exceptions.InvalidResetTokenException;
import com.saveplate.api.mapper.UserMapper;
import com.saveplate.api.repositories.UserRepository;
import com.saveplate.api.security.JwtService;
import com.saveplate.api.service.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new EmailAlreadyExistsException(request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user= userMapper.toEntity(request,encodedPassword);
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(),user.getRole().name());
        return new AuthResponse(token,user.getEmail(),user.getRole().name());
    }
    @Override
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur existe avec ce email: " + request.email()));
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }
    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request){
        Optional<User> user =userRepository.findByEmail(request.email());
        String resetToken = user.map(u -> jwtService.generatePasswordResetToken(u.getEmail(),u.getPassword())).orElse(null);

        return new ForgotPasswordResponse("Si un compte existe avec cet email, un token de réinitialisation a été généré.",resetToken);
    }
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email;
        try {
            email = jwtService.extractEmail(request.token());
        } catch (JwtException e) {
            throw new InvalidResetTokenException();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidResetTokenException::new);

        if (!jwtService.isPasswordResetTokenValid(request.token(), user.getPassword())) {
            throw new InvalidResetTokenException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

}
