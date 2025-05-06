package com.rage.ecommerce.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.order.*;
import com.rage.ecommerce.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException;

    Optional<Order> getOrderById(UUID orderId);

    void checkOffer(CheckOfferRequestDTO checkOfferRequestDTO) throws JsonProcessingException;

    void applyOffer(ApplyOfferRequestDTO applyOfferRequestDTO) throws JsonProcessingException;

    boolean cancelOffer(UUID orderId);
    boolean placeOrder(UUID orderId);
    void makePayment(MakePaymentRequestDTO makePaymentRequestDTO) throws JsonProcessingException;
    void processPayment(GeneratedPaymentStatusRequestDTO generatedPaymentStatusRequestDTO) throws JsonProcessingException;
    void shipOrder(GeneratedPaymentStatusRequestDTO generatedPaymentStatusRequestDTO) throws JsonProcessingException;
    boolean deliverOrder(UUID orderId);
    boolean cancelOrder(UUID orderId);
    boolean returnOrder(UUID orderId);
}
