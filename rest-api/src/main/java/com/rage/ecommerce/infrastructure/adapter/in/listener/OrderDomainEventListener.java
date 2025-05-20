package com.rage.ecommerce.infrastructure.adapter.in.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rage.ecommerce.application.dto.order.*;
import com.rage.ecommerce.domain.port.in.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.rage.ecommerce.infrastructure.adapter.out.KafkaEventTypes.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDomainEventListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id.order-events}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(value = "event-type", required = false) String eventTypeHeader,
                       @Header(value = "event-version", required = false) String eventVersionHeader,
                       @Header(value = "correlation-id", required = false) String correlationIdHeader
    ) {
        if (eventTypeHeader == null) {
            log.warn("Received message on topic '{}' with no 'event-type' header. Skipping: {}", topic, consumerRecord.value());
            return;
        }

        log.info("Received event type '{}' from topic '{}', key: {}, value: {}",
                eventTypeHeader, topic, consumerRecord.key(), consumerRecord.value());

        try {
            switch (eventTypeHeader) {
                case ORDER_CREATED:
                    var checkOfferEvent = objectMapper.readValue(consumerRecord.value(), CheckOfferRequestDTO.class);
                    orderService.checkOffer(checkOfferEvent, correlationIdHeader);
                    break;
                case ORDER_OFFER_CHECKED:
                    var applyOfferEvent = objectMapper.readValue(consumerRecord.value(), ApplyOfferRequestDTO.class);
                    orderService.applyOffer(applyOfferEvent, correlationIdHeader);
                    break;
                case ORDER_OFFER_APPLIED:
                    var orderPaidEvent = objectMapper.readValue(consumerRecord.value(), MakePaymentRequestDTO.class);
                    orderService.makePayment(orderPaidEvent, correlationIdHeader);
                    break;
                case ORDER_PAYMENT_PROCESSED:
                    var paymentProcessEvent = objectMapper.readValue(consumerRecord.value(), GeneratedPaymentStatusRequestDTO.class);
                    orderService.processPaymentStatus(paymentProcessEvent, correlationIdHeader);
                    break;
                case ORDER_PAYMENT_SUCCESSFUL:
                    var orderShippedEvent = objectMapper.readValue(consumerRecord.value(), PaymentSuccessRequestDTO.class);
                    orderService.postProcessOrder(orderShippedEvent, correlationIdHeader);
                    break;
                case ORDER_POSTPROCESSED:
                    var orderDeliveredEvent = objectMapper.readValue(consumerRecord.value(), ShipOrderRequestDTO.class);
                    orderService.shipOrder(orderDeliveredEvent, correlationIdHeader);
                    break;
                case ORDER_SHIPPED:
                    var postProcessedEvent = objectMapper.readValue(consumerRecord.value(), DeliverOrderRequestDTO.class);
                    orderService.deliverOrder(postProcessedEvent, correlationIdHeader);
                    break;
                default:
                    log.warn("Unknown event type '{}' received. No handler found.", eventTypeHeader);
                    break;
            }
        } catch (IOException e) {
            log.error("Error processing event type '{}' message: {}", eventTypeHeader, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unhandled exception while processing event type '{}': {}", eventTypeHeader, e.getMessage(), e);
        }
    }
}
