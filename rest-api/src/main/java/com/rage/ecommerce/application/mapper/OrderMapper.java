package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;

public class OrderMapper {


    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderState(order.getOrderState());
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .orderState(entity.getOrderState())
                .build();
    }

    public static Order toDomain(CreateOrderRequestDTO dto) {
        return Order.builder()
                .itemName(dto.getItemName())
                .customerName(dto.getCustomerName())
                .build();
    }

    public static CreateOrderResponseDTO toCreateOrderResponseDTO(Order order) {
        return CreateOrderResponseDTO.builder()
                .id(order.getId())
                .orderState(order.getOrderState())
                .customerName(order.getCustomerName())
                .itemName(order.getItemName())
                .build();
    }
}
