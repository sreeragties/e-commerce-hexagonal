package com.rage.ecommerce.drools.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.drools.application.dto.OfferEvaluationRequestDTO;

public interface RuleService {

    void handleAndExecuteRules(OfferEvaluationRequestDTO dto, String key, String correlationIdHeader) throws JsonProcessingException;
}
