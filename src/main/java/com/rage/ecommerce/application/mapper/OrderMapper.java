package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;

public class OrderMapper {

//    public static Order toDomain(com.rage.ecommerce.application.dto.OrderDTO dto) {
//        return new Order();
//    }
//
//    public static com.rage.ecommerce.application.dto.OrderDTO toDTO(Order order) {
//        com.rage.ecommerce.application.dto.OrderDTO dto = new com.rage.ecommerce.application.dto.OrderDTO();
//        return dto;
//    }

    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderState(order.getOrderState());
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        Order order = new Order();
        order.setId(entity.getId());
        order.setOrderState(entity.getOrderState());
        return order;
    }
}
