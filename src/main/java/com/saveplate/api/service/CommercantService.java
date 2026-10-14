package com.saveplate.api.service;

import com.saveplate.api.dto.admin.CommercantResponse;
import com.saveplate.api.dto.admin.PromoteCommercantRequest;
import org.springframework.stereotype.Component;


public interface CommercantService {
CommercantResponse promoteToCommercant(PromoteCommercantRequest request);
}
