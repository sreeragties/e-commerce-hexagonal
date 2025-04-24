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
        entity.setItemName(order.getItemName());
        entity.setCustomerName(order.getCustomerName());
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        return Order.builder()
                .processId(entity.getProcessId())
                .orderState(entity.getOrderState())
                .customerName(entity.getCustomerName())
                .itemName(entity.getItemName())
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
                .processId(order.getProcessId())
                .orderState(order.getOrderState())
                .customerName(order.getCustomerName())
                .itemName(order.getItemName())
                .build();
    }
}
