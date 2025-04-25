package com.rage.ecommerce.domain.port.out.repository;

import com.rage.ecommerce.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Optional<Order> findById(UUID orderId);

    Order save(Order order);
}
