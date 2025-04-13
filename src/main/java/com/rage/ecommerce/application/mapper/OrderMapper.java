package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.OrderDTO;
import com.rage.ecommerce.domain.model.Order;

public class OrderMapper {

    public static Order toDomain(OrderDTO dto) {
        return new Order();
    }

    public static OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        return dto;
    }
}
