package com.rage.ecommerce.drools.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.drools.application.dto.OfferEvaluationRequestDTO;
import com.rage.ecommerce.drools.application.dto.OfferEvaluationResponseDTO;
import com.rage.ecommerce.drools.application.mapper.OfferMapper;
import com.rage.ecommerce.drools.domain.model.Decision;
import com.rage.ecommerce.drools.domain.model.Offer;
import com.rage.ecommerce.drools.domain.model.enums.CustomerSubscription;
import com.rage.ecommerce.drools.domain.port.in.RuleService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.rage.ecommerce.drools.infrastructure.config.DroolsConfig.PREMIUM_SESSION_NAME;
import static com.rage.ecommerce.drools.infrastructure.config.DroolsConfig.STANDARD_SESSION_NAME;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RuleServiceImpl.class);

    private final KieContainer kieContainer;

    private final OfferMapper offerMapper;

    @Value(value = "${kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void handleAndExecuteRules(OfferEvaluationRequestDTO dto, String key) throws JsonProcessingException {
        log.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto);
        var offer = offerMapper.toOffer(dto);
        CustomerSubscription subscription = determineSubscription(offer);
        var decision = executeRules(offer, subscription);
        OfferEvaluationResponseDTO response = OfferEvaluationResponseDTO.builder()
                .processId(dto.getProcessId())
                .orderState(dto.getOrderState())
                .itemId(dto.getItemId())
                .customerId(dto.getCustomerId())
                .dateOfBirth(dto.getDateOfBirth())
                .subscription(dto.getSubscription())
                .itemOfferLevel(dto.getItemOfferLevel())
                .reason(decision.getReason())
                .offerRate(decision.getOfferRate())
                .build();

        sendProducerMessage(response.getClass().getSimpleName(), response, dto.getProcessId());
    }
    private CustomerSubscription determineSubscription(Offer dto) {
        var subscription = dto.getSubscription();
        return switch (subscription) {
            case PREMIUM -> CustomerSubscription.PREMIUM;
            case STANDARD -> CustomerSubscription.STANDARD;
        };
    }

    public Decision executeRules(Object fact, CustomerSubscription subscription) {
        String sessionName = subscription == CustomerSubscription.PREMIUM
                ? PREMIUM_SESSION_NAME
                : STANDARD_SESSION_NAME;

        log.info("Executing rules using session: {}", sessionName);

        Decision finalDecision = null;

        KieSession kieSession = kieContainer.newKieSession(sessionName);
        try {
            kieSession.insert(fact);
            kieSession.fireAllRules();

            java.util.Collection<?> objects = kieSession.getObjects(o -> o instanceof com.rage.ecommerce.drools.domain.model.Decision);

            if (!objects.isEmpty()) {
                // Assuming only one Decision object is expected per rule execution
                finalDecision = (com.rage.ecommerce.drools.domain.model.Decision) objects.iterator().next();
                log.info("Retrieved Decision: OfferRate={}, Reason={}", finalDecision.getOfferRate(), finalDecision.getReason());
            } else {
                finalDecision = Decision.builder()
                        .offerRate(0.0)
                        .reason("No rule matched")
                        .build();
                log.info("No Decision object was inserted by the rules.");
            }

        } finally {
            kieSession.dispose();
        }

        return finalDecision;
    }

    public void executeRules(Object fact) {
        executeRules(fact, CustomerSubscription.STANDARD);
    }

    private <T> void sendProducerMessage(String className, T message, UUID processId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String serialisedResponse = objectMapper.writeValueAsString(message);
        String serialisedProcessId = processId.toString();

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("DTOClassName", className.getBytes()));

        ProducerRecord<String, String> record = new ProducerRecord <>(topicName, null, serialisedProcessId, serialisedResponse, headers);
        kafkaTemplate.send(record);
    }
}
