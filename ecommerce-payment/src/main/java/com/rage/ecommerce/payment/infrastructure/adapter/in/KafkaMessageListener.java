package com.rage.ecommerce.payment.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.payment.application.dto.MakePaymentResponseDTO;
import com.rage.ecommerce.payment.application.dto.ProcessPaymentRequestDTO;
import com.rage.ecommerce.payment.application.mapper.PaymentMapper;
import com.rage.ecommerce.payment.domain.port.in.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {

    private final PaymentService paymentService;

    private final PaymentMapper paymentMapper;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            var requestDto = objectMapper.readValue(record.value(), MakePaymentResponseDTO.class);
            ProcessPaymentRequestDTO dto = paymentMapper.toProcessPaymentRequestDTO(requestDto);
            paymentService.handleAndExecutePayment(dto, record.key());
        } catch (IOException e) {
            log.error("Error processing CheckOrderResponseDTO message: {}", e.getMessage());
        }
    }
}

