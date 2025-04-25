package com.rage.ecommerce.infrastructure.adapter.out.repository.order;

import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
