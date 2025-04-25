package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.order.CheckOrderResponseDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.infrastructure.adapter.out.CustomerEntity;
import com.rage.ecommerce.infrastructure.adapter.out.ItemEntity;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "itemId", target = "item", qualifiedByName = "mapItemIdToEntity")
    @Mapping(source = "customerId", target = "customer", qualifiedByName = "mapCustomerIdToEntity")
    OrderEntity toEntity(Order order);

    @Mapping(source = "item.itemId", target = "itemId")
    @Mapping(source = "customer.customerId", target = "customerId")
    Order toDomain(OrderEntity entity);

    Order toDomain(CreateOrderRequestDTO dto);

    CreateOrderResponseDTO toCreateOrderResponseDTO(Order order);

    @Named("mapItemIdToEntity")
    static ItemEntity mapItemIdToEntity(UUID itemId) {
        return ItemEntity.builder().itemId(itemId).build();
    }

    @Named("mapCustomerIdToEntity")
    static CustomerEntity mapCustomerIdToEntity(UUID customerId) {
        return CustomerEntity.builder().customerId(customerId).build();
    }

    Order toDomain(CheckOrderResponseDTO dto);

    CheckOrderResponseDTO toCheckOrderResponseDTO(Order order);
}