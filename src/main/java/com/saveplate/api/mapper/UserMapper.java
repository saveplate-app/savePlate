package com.saveplate.api.mapper;

import com.saveplate.api.dto.auth.RegisterRequest;
import com.saveplate.api.entities.User;
import com.saveplate.api.entities.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public User toEntity(RegisterRequest request,String encodedPassword)
    {
        User user = new User();
        user.setFirstName(request.firstname());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(encodedPassword);
        user.setDateInscription(LocalDateTime.now());
        user.setRole(Role.CLIENT);
        return user;
    }
}
