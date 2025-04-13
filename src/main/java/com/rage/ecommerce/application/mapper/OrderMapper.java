package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.domain.model.Order;

public class OrderMapper {

    public static Order toDomain(com.rage.ecommerce.application.dto.OrderDTO dto) {
        return new Order();
    }

    public static com.rage.ecommerce.application.dto.OrderDTO toDTO(Order order) {
        com.rage.ecommerce.application.dto.OrderDTO dto = new com.rage.ecommerce.application.dto.OrderDTO();
        return dto;
    }
}
