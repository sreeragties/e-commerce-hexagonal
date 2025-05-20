package com.rage.ecommerce.payment.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.payment.application.dto.ProcessPaymentRequestDTO;
import com.rage.ecommerce.payment.application.mapper.PaymentMapper;
import com.rage.ecommerce.payment.domain.enums.PaymentStatus;
import com.rage.ecommerce.payment.domain.port.in.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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
    public void handleAndExecutePayment(ProcessPaymentRequestDTO dto, String key, String correlationIdHeader) throws JsonProcessingException {

        log.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto);
        var response = paymentMapper.toProcessPaymentResponseDTO(dto);
        response.setPaymentStatus(PaymentStatus.SUCCESS);

        sendProducerMessage("order.payment.processed", "v1.0", response, correlationIdHeader, response.getProcessId().toString());
    }

    public <T> void sendProducerMessage(String kafkaEventType, String eventVersion, T message, String correlationId, String messageKey) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String serialisedResponse = objectMapper.writeValueAsString(message);

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("event-type", kafkaEventType.getBytes()));
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
