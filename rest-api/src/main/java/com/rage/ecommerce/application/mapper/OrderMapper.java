package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.order.*;
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

    Order toDomain(CreateOrderResponseDTO dto);

    @Named("mapItemIdToEntity")
    static ItemEntity mapItemIdToEntity(UUID itemId) {
        return ItemEntity.builder().itemId(itemId).build();
    }

    @Named("mapCustomerIdToEntity")
    static CustomerEntity mapCustomerIdToEntity(UUID customerId) {
        return CustomerEntity.builder().customerId(customerId).build();
    }

    Order toDomain(CheckOfferResponseDTO dto);

    CheckOfferResponseDTO toCheckOrderResponseDTO(Order order);

    Order toDomain(OfferEvaluationResponseDTO dto);

    Order toDomain(ApplyOfferResponseDTO dto);

    ApplyOfferResponseDTO toApplyOfferResponseDTO(Order order);

    Order toDomain(MakePaymentResponseDTO dto);

    MakePaymentResponseDTO toMakePaymentResponseDTO(Order order);

    GeneratedPaymentStatusResponseDTO toGeneratedPaymentStatusResponseDTO(Order order);

    PaymentSuccessResponseDTO toPaymentSuccessResponseDTO(Order order);

    ShipOrderResponseDTO toShipOrderResponseDTO(Order order);

    DeliverOrderResponseDTO toDeliverOrderResponseDTO(Order order);
}