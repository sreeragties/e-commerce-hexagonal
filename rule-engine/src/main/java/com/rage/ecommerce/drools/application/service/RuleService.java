package com.rage.ecommerce.drools.application.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private final KieContainer kieContainer;

    @Autowired
    public RuleService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public void executeRules(Object fact) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(fact);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
