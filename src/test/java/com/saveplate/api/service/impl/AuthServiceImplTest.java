package com.saveplate.api.service.impl;


import com.saveplate.api.dto.auth.AuthResponse;
import com.saveplate.api.dto.auth.RegisterRequest;
import com.saveplate.api.entities.User;
import com.saveplate.api.entities.enums.Role;
import com.saveplate.api.exceptions.EmailAlreadyExistsException;
import com.saveplate.api.mapper.UserMapper;
import com.saveplate.api.repositories.UserRepository;
import com.saveplate.api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
