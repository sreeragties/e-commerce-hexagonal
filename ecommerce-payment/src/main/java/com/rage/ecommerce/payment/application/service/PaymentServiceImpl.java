package com.rage.ecommerce.payment.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.payment.application.dto.ProcessPaymentRequestDTO;
import com.rage.ecommerce.payment.application.mapper.PaymentMapper;
import com.rage.ecommerce.payment.domain.port.in.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;

    @Value(value = "${kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void handleAndExecutePayment(ProcessPaymentRequestDTO dto, String key) throws JsonProcessingException {

        log.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto);
        var response = paymentMapper.toProcessPaymentResponseDTO(dto);

        sendProducerMessage(response.getClass().getSimpleName(), response, dto.getProcessId());
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
