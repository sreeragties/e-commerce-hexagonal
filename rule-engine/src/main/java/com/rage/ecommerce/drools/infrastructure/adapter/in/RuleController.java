package com.rage.ecommerce.drools.infrastructure.adapter.in;

import com.rage.ecommerce.drools.application.service.RuleServiceImpl;
import com.rage.ecommerce.drools.domain.model.Fact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleController {

    private final RuleServiceImpl ruleServiceImpl;

    @Autowired
    public RuleController(RuleServiceImpl ruleServiceImpl) {
        this.ruleServiceImpl = ruleServiceImpl;
    }

    @PostMapping("/execute-rules")
    public Fact executeRules(@RequestBody Fact fact) {
        ruleServiceImpl.executeRules(fact);
        return fact;

    }
}
