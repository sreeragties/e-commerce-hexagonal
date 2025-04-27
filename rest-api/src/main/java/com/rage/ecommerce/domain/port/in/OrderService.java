package com.rage.ecommerce.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.order.CheckOfferResponseDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException;

    Optional<Order> getOrderById(UUID orderId);

    CheckOfferResponseDTO checkOffer(UUID orderId) throws JsonProcessingException;

    boolean applyOffer(UUID orderId);
    boolean cancelOffer(UUID orderId);
    boolean placeOrder(UUID orderId);
    boolean makePayment(UUID orderId, boolean paymentSuccessful);
    boolean shipOrder(UUID orderId);
    boolean deliverOrder(UUID orderId);
    boolean cancelOrder(UUID orderId);
    boolean returnOrder(UUID orderId);
}
