package com.rage.ecommerce.drools.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.drools.application.dto.ApplyOfferRequestDTO;

public interface RuleService {

    void handleAndExecuteRules(ApplyOfferRequestDTO dto, String key) throws JsonProcessingException;
}
