package com.rage.ecommerce.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.order.*;
import com.rage.ecommerce.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException;

    Optional<Order> getOrderById(UUID orderId);

    void checkOffer(CheckOfferRequestDTO checkOfferRequestDTO, String correlationIdHeader) throws JsonProcessingException;

    void applyOffer(ApplyOfferRequestDTO applyOfferRequestDTO, String correlationIdHeader) throws JsonProcessingException;

    boolean cancelOffer(UUID orderId);
    boolean placeOrder(UUID orderId);
    void makePayment(MakePaymentRequestDTO makePaymentRequestDTO, String correlationIdHeader) throws JsonProcessingException;
    void processPaymentStatus(GeneratedPaymentStatusRequestDTO generatedPaymentStatusRequestDTO, String correlationIdHeader) throws JsonProcessingException;
    void postProcessOrder(PaymentSuccessRequestDTO paymentSuccessRequestDTO, String correlationIdHeader) throws JsonProcessingException;
    void shipOrder(ShipOrderRequestDTO shipOrderRequestDTO, String correlationIdHeader) throws JsonProcessingException;
    void deliverOrder(DeliverOrderRequestDTO deliverOrderRequestDTO, String correlationIdHeader) throws JsonProcessingException;
    boolean cancelOrder(UUID orderId);
    boolean returnOrder(UUID orderId);
}
