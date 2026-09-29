package com.saveplate.api.service.impl;


import com.saveplate.api.dto.auth.AuthResponse;
import com.saveplate.api.dto.auth.ForgotPasswordRequest;
import com.saveplate.api.dto.auth.ForgotPasswordResponse;
import com.saveplate.api.dto.auth.LoginRequest;
import com.saveplate.api.dto.auth.RegisterRequest;
import com.saveplate.api.dto.auth.ResetPasswordRequest;
import com.saveplate.api.entities.User;
import com.saveplate.api.entities.enums.Role;
import com.saveplate.api.exceptions.EmailAlreadyExistsException;
import com.saveplate.api.exceptions.InvalidResetTokenException;
import com.saveplate.api.mapper.UserMapper;
import com.saveplate.api.repositories.UserRepository;
import com.saveplate.api.security.JwtService;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void register_shouldCreateUserAndReturnToken_withEmailIsnotTaken(){
        RegisterRequest request = new RegisterRequest("saad","boumahdi","saadboumahdi@gmail.com","password123");

        User mappedUser = new User();
        mappedUser.setEmail("saadboumahdi@gmail.com");
        mappedUser.setRole(Role.CLIENT);
        when(userRepository.findByEmail("saadboumahdi@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userMapper.toEntity(request,"hashedPassword")).thenReturn(mappedUser);
        when(jwtService.generateToken("saadboumahdi@gmail.com","CLIENT")).thenReturn("fake.jwt.token");
        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("fake.jwt.token");
        assertThat(response.email()).isEqualTo("saadboumahdi@gmail.com");
        assertThat(response.role()).isEqualTo("CLIENT");
        verify(userRepository).save(mappedUser);
    }

    @Test
    void register_shouldCreateUserAndReturnToken_withEmailIsTaken(){
        RegisterRequest request = new RegisterRequest("saad","boumahdi","saadboumahdi@gmail.com","password123");

        when(userRepository.findByEmail("saadboumahdi@gmail.com")).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyExistsException.class,()->authService.register(request));
        verify(userRepository,never()).save(any());
    }
    @Test
    void login_shouldReturnToken_whenCredentialsAreValid(){
        String email ="saadboumahdi@gmail.com";
        LoginRequest request = new LoginRequest(email,"password123");
        User user = new User();
        user.setEmail(email);
        user.setRole(Role.CLIENT);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(email,"CLIENT")).thenReturn("fake.jwt.token");

        AuthResponse response = authService.login(request);
        assertThat(response.token()).isEqualTo("fake.jwt.token");
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.role()).isEqualTo("CLIENT");

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_shouldThrowBadCredentialsException_whenCredentialsAreInvalid(){
        String email ="saadboumahdi@gmail.com";
        LoginRequest request = new LoginRequest(email,"wrongPassword");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad Credentails"));
        assertThrows(BadCredentialsException.class,()-> authService.login(request));
        verify(jwtService,never()).generateToken(any(),any());

    }

    @Test
    void forgotPassword_shouldReturnResetToken_whenEmailExists(){
        String email = "saadboumahdi@gmail.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedPassword");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generatePasswordResetToken(email,"hashedPassword")).thenReturn("fake.reset.token");

        ForgotPasswordResponse response = authService.forgotPassword(request);

        assertThat(response.resetToken()).isEqualTo("fake.reset.token");
        assertThat(response.message()).isNotBlank();
    }

    @Test
    void forgotPassword_shouldReturnNullResetToken_whenEmailDoesNotExist(){
        String email = "unknown@gmail.com";
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ForgotPasswordResponse response = authService.forgotPassword(request);

        assertThat(response.resetToken()).isNull();
        verify(jwtService, never()).generatePasswordResetToken(any(), any());
    }

    @Test
    void resetPassword_shouldUpdatePasswordHash_whenTokenIsValid(){
        String email = "saadboumahdi@gmail.com";
        ResetPasswordRequest request = new ResetPasswordRequest("valid.reset.token", "newPassword123");
        User user = new User();
        user.setEmail(email);
        user.setPassword("oldHashedPassword");
        when(jwtService.extractEmail("valid.reset.token")).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isPasswordResetTokenValid("valid.reset.token","oldHashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newHashedPassword");

        authService.resetPassword(request);

        assertThat(user.getPassword()).isEqualTo("newHashedPassword");
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_shouldThrowInvalidResetTokenException_whenTokenFailsValidation(){
        String email = "saadboumahdi@gmail.com";
        ResetPasswordRequest request = new ResetPasswordRequest("stale.reset.token", "newPassword123");
        User user = new User();
        user.setEmail(email);
        user.setPassword("oldHashedPassword");
        when(jwtService.extractEmail("stale.reset.token")).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isPasswordResetTokenValid("stale.reset.token","oldHashedPassword")).thenReturn(false);

        assertThrows(InvalidResetTokenException.class, ()-> authService.resetPassword(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetPassword_shouldThrowInvalidResetTokenException_whenUserNoLongerExists(){
        ResetPasswordRequest request = new ResetPasswordRequest("orphaned.token", "newPassword123");
        when(jwtService.extractEmail("orphaned.token")).thenReturn("deleted@gmail.com");
        when(userRepository.findByEmail("deleted@gmail.com")).thenReturn(Optional.empty());

        assertThrows(InvalidResetTokenException.class, ()-> authService.resetPassword(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetPassword_shouldThrowInvalidResetTokenException_whenTokenIsMalformed(){
        ResetPasswordRequest request = new ResetPasswordRequest("garbage.token", "newPassword123");
        when(jwtService.extractEmail("garbage.token")).thenThrow(new MalformedJwtException("bad token"));

        assertThrows(InvalidResetTokenException.class, ()-> authService.resetPassword(request));
        verify(userRepository, never()).findByEmail(any());
    }

}
