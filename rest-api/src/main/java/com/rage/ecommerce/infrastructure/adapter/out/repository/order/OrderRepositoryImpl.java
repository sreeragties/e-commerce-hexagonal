package com.rage.ecommerce.infrastructure.adapter.out.repository.order;

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
    private final OrderMapper orderMapper;

    @Override
    public Optional<Order> findById(UUID processId) {
        OrderEntity orderEntity = jpaOrderRepository.findById(processId).orElseThrow(() -> new RuntimeException("Order not found with processId: " + processId));
        return Optional.of(orderMapper.toDomain(orderEntity));
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);
        return orderMapper.toDomain(jpaOrderRepository.save(orderEntity));
    }
}
