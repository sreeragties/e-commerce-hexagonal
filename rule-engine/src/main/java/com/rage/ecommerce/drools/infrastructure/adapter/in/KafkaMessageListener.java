package com.rage.ecommerce.drools.infrastructure.adapter.in;

import com.rage.ecommerce.drools.application.dto.OfferEvaluationRequestDTO;
import com.rage.ecommerce.drools.application.dto.CheckOfferResponseDTO;
import com.rage.ecommerce.drools.application.mapper.OfferMapper;
import com.rage.ecommerce.drools.application.service.RuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KafkaMessageListener.class);

    private final RuleServiceImpl ruleService;

    private final OfferMapper offerMapper;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            var requestDto = objectMapper.readValue(record.value(), CheckOfferResponseDTO.class);
            OfferEvaluationRequestDTO dto = offerMapper.toApplyOfferRequestDTOFromCheckOfferResponse(requestDto);
            ruleService.handleAndExecuteRules(dto, record.key());
        } catch (IOException e) {
            log.error("Error processing CheckOrderResponseDTO message: {}", e.getMessage());
        }
    }
}

