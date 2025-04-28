package com.rage.ecommerce.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.application.dto.order.OfferEvaluationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfferEvaluationMessageListener {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id}",
    containerFactory = "offerEvaluationResponseContainerFactory")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            var requestDto = objectMapper.readValue(record.value(), OfferEvaluationResponseDTO.class);
        } catch (IOException e) {
            log.error("Error processing CheckOrderResponseDTO message: {}", e.getMessage());
        }
    }
}

