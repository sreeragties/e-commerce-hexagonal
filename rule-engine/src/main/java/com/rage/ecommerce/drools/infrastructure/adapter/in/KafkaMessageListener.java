package com.rage.ecommerce.drools.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rage.ecommerce.drools.application.dto.OfferEvaluationRequestDTO;
import com.rage.ecommerce.drools.application.dto.CheckOfferResponseDTO;
import com.rage.ecommerce.drools.application.mapper.OfferMapper;
import com.rage.ecommerce.drools.application.service.RuleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {

    private final RuleServiceImpl ruleService;

    private final OfferMapper offerMapper;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id}")
    public void listen(ConsumerRecord<String, String> consumerRecord,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(value = "event-type", required = false) String eventTypeHeader,
                       @Header(value = "event-version", required = false) String eventVersionHeader,
                       @Header(value = "correlation-id", required = false) String correlationIdHeader
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            var requestDto = objectMapper.readValue(consumerRecord.value(), CheckOfferResponseDTO.class);
            OfferEvaluationRequestDTO dto = offerMapper.toApplyOfferRequestDTOFromCheckOfferResponse(requestDto);
            if(Objects.equals(eventTypeHeader, "order.offer.ready.to.check")) {
                ruleService.handleAndExecuteRules(dto, consumerRecord.key(), correlationIdHeader);
            }
        } catch (IOException e) {
            log.error("Error processing CheckOrderResponseDTO message: {}", e.getMessage());
        }
    }
}

