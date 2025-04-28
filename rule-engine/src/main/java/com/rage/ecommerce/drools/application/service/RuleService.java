package com.rage.ecommerce.drools.application.service;

import com.rage.ecommerce.drools.application.dto.ApplyOfferRequestDTO;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RuleService.class);

    private final KieContainer kieContainer;

    public void handleAndExecuteRules(ApplyOfferRequestDTO dto, String key) {
        log.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto);
        executeRules(dto);
    }

    public void executeRules(Object fact) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(fact);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
