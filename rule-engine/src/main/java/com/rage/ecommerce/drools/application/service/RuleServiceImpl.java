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
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.rage.ecommerce.drools.infrastructure.config.DroolsConfig.PREMIUM_SESSION_NAME;
import static com.rage.ecommerce.drools.infrastructure.config.DroolsConfig.STANDARD_SESSION_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleServiceImpl implements RuleService {

    private final KieContainer kieContainer;

    private final OfferMapper offerMapper;

    @Value(value = "${kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void handleAndExecuteRules(OfferEvaluationRequestDTO dto, String key, String correlationId) throws JsonProcessingException {
        log.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto);
        var offer = offerMapper.toOffer(dto);
        CustomerSubscription subscription = determineSubscription(offer);
        var decision = executeRules(offer, subscription);
        OfferEvaluationResponseDTO response = OfferEvaluationResponseDTO.builder()
                .processId(dto.getProcessId())
                .reason(decision.getReason())
                .offerRate(decision.getOfferRate())
                .build();

        sendProducerMessage("order.offer.checked", "v1.0", response, correlationId, response.getProcessId().toString());
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

    public <T> void sendProducerMessage(String kafkaEventType, String eventVersion, T message, String correlationId, String messageKey) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String serialisedResponse = objectMapper.writeValueAsString(message);

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("event-type", "order.offer.checked".getBytes()));
        headers.add(new RecordHeader("event-version", "v1.0".getBytes()));
        if (correlationId != null) {
            headers.add(new RecordHeader("correlation-id", correlationId.getBytes(StandardCharsets.UTF_8)));
        } else {
            log.warn("Sending Kafka event '{}' without a correlation ID. Consider propagating one.", kafkaEventType);
        }
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                topicName,
                null,
                messageKey,
                serialisedResponse,
                headers
        );

        kafkaTemplate.send(producerRecord);

        log.info("Sent event '{}' (v{}) with correlation ID '{}' to topic '{}' with key '{}'",
                kafkaEventType, eventVersion, correlationId, topicName, messageKey);
    }
}
