package com.rage.ecommerce.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.order.*;
import com.rage.ecommerce.domain.model.Order;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException;

    Optional<Order> getOrderById(UUID orderId);

    CheckOfferResponseDTO checkOffer(UUID orderId) throws JsonProcessingException;

    void applyOffer(Order order) throws JsonProcessingException;

    boolean cancelOffer(UUID orderId);
    boolean placeOrder(UUID orderId);
    boolean makePayment(UUID orderId, boolean paymentSuccessful);
    boolean shipOrder(UUID orderId);
    boolean deliverOrder(UUID orderId);
    boolean cancelOrder(UUID orderId);
    boolean returnOrder(UUID orderId);
}
