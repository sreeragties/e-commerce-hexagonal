package com.rage.ecommerce.drools.application.service;

import com.rage.ecommerce.drools.application.dto.ApplyOfferRequestDTO;
import com.rage.ecommerce.drools.application.mapper.OfferMapper;
import com.rage.ecommerce.drools.domain.model.Offer;
import com.rage.ecommerce.drools.domain.model.enums.CustomerSubscription;
import com.rage.ecommerce.drools.domain.port.in.RuleService;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import static com.rage.ecommerce.drools.infrastructure.config.DroolsConfig.PREMIUM_SESSION_NAME;
import static com.rage.ecommerce.drools.infrastructure.config.DroolsConfig.STANDARD_SESSION_NAME;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RuleServiceImpl.class);

    private final KieContainer kieContainer;

    private final OfferMapper offerMapper;

    public void handleAndExecuteRules(ApplyOfferRequestDTO dto, String key) {
        log.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto);
        var offer = offerMapper.toOffer(dto);
        CustomerSubscription subscription = determineSubscription(offer);
        executeRules(offer, subscription);
    }
    private CustomerSubscription determineSubscription(Offer dto) {
        var subscription = dto.getSubscription();
        return switch (subscription) {
            case PREMIUM -> CustomerSubscription.PREMIUM;
            case STANDARD -> CustomerSubscription.STANDARD;
        };
    }

    public void executeRules(Object fact, CustomerSubscription subscription) {
        String sessionName = subscription == CustomerSubscription.PREMIUM
                ? PREMIUM_SESSION_NAME
                : STANDARD_SESSION_NAME;

        log.info("Executing rules using session: {}", sessionName);

        KieSession kieSession = kieContainer.newKieSession(sessionName);
        try {
            kieSession.insert(fact);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
    }

    // For backward compatibility
    public void executeRules(Object fact) {
        executeRules(fact, CustomerSubscription.STANDARD);
    }
}
