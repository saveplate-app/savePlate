package com.saveplate.api.service.impl;

import com.saveplate.api.dto.admin.CommercantResponse;
import com.saveplate.api.dto.admin.PromoteCommercantRequest;
import com.saveplate.api.entities.Commercant;
import com.saveplate.api.entities.User;
import com.saveplate.api.entities.enums.Role;
import com.saveplate.api.exceptions.AlreadyCommercantException;
import com.saveplate.api.exceptions.UserNotFoundException;
import com.saveplate.api.mapper.CommercantMapper;
import com.saveplate.api.repositories.CommercantRepository;
import com.saveplate.api.repositories.UserRepository;
import com.saveplate.api.service.CommercantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommercantServiceImpl implements CommercantService {
    private final CommercantMapper commercantMapper;
    private final UserRepository userRepository;
    private final CommercantRepository commercantRepository;
    @Override
    @Transactional
    public CommercantResponse promoteToCommercant(PromoteCommercantRequest request){

        User user = userRepository.findByEmail(request.email()).orElseThrow(()-> new UserNotFoundException(request.email()));
        if(user.getRole() != Role.CLIENT){
           throw  new AlreadyCommercantException(request.email());
        }
        user.setRole(Role.COMMERCANT);
        userRepository.save(user);
        Commercant commercant = commercantMapper.toEntity(request,user);
        Commercant saved = commercantRepository.save(commercant);
        return commercantMapper.toResponse(saved);

    }
}
