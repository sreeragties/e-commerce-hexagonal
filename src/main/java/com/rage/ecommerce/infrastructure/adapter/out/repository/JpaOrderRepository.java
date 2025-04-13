package com.rage.ecommerce.infrastructure.adapter.out.repository;

import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
