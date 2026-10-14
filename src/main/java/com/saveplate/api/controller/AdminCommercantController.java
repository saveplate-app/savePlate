package com.saveplate.api.controller;

import com.saveplate.api.dto.admin.CommercantResponse;
import com.saveplate.api.dto.admin.PromoteCommercantRequest;
import com.saveplate.api.service.CommercantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/commercants")
@RequiredArgsConstructor
public class AdminCommercantController {
    private final CommercantService commercantService;

    @PostMapping
    public ResponseEntity<CommercantResponse> promoteToCommercant(@Valid @RequestBody PromoteCommercantRequest request){
    CommercantResponse response = commercantService.promoteToCommercant(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
