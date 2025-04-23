package com.rage.ecommerce.infrastructure.adapter.out.repository;

import com.rage.ecommerce.application.mapper.OrderMapper;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.domain.port.out.repository.OrderRepository;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Optional<Order> findById(UUID orderId) {
        OrderEntity orderEntity = jpaOrderRepository.findById(orderId).orElseThrow();
        return Optional.of(OrderMapper.toDomain(orderEntity));
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = OrderMapper.toEntity(order);
        return OrderMapper.toDomain(jpaOrderRepository.save(orderEntity));
    }
}
