package com.rage.ecommerce.infrastructure.adapter.in.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.application.mapper.OrderMapper;
import com.rage.ecommerce.domain.port.in.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOrderMessageListener {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id.create-order}",
    containerFactory = "createOrderResponseContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            var requestDto = objectMapper.readValue(consumerRecord.value(), CreateOrderResponseDTO.class);
            var order = orderMapper.toDomain(requestDto);
            orderService.checkOffer(order.getProcessId());
        } catch (IOException e) {
            log.error("Error processing CheckOfferResponseDTO message: {}", e.getMessage());
        }
    }
}
