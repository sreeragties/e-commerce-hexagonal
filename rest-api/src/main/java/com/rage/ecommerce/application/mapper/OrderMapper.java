package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;

public class OrderMapper {


    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setProcessId(order.getProcessId());
        entity.setOrderState(order.getOrderState());
        entity.setItemId(order.getItemId());
        entity.setCustomerId(order.getCustomerId());
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        return Order.builder()
                .processId(entity.getProcessId())
                .orderState(entity.getOrderState())
                .customerId(entity.getCustomerId())
                .itemId(entity.getItemId())
                .build();
    }

    public static Order toDomain(CreateOrderRequestDTO dto) {
        return Order.builder()
                .itemId(dto.getItemId())
                .customerId(dto.getCustomerId())
                .build();
    }

    public static CreateOrderResponseDTO toCreateOrderResponseDTO(Order order) {
        return CreateOrderResponseDTO.builder()
                .processId(order.getProcessId())
                .orderState(order.getOrderState())
                .customerId(order.getCustomerId())
                .itemId(order.getItemId())
                .build();
    }
}
