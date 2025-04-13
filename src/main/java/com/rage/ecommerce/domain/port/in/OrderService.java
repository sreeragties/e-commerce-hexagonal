package com.rage.ecommerce.domain.port.in;

import com.rage.ecommerce.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    Order createOrder();

    Optional<Order> getOrderById(UUID orderId);

    boolean placeOrder(UUID orderId);
    boolean applyOffer(UUID orderId);
    boolean makePayment(UUID orderId, boolean paymentSuccessful);
    boolean shipOrder(UUID orderId);
    boolean deliverOrder(UUID orderId);
    boolean cancelOrder(UUID orderId);
    boolean returnOrder(UUID orderId);
}
