package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.item.CreateItemRequestDTO;
import com.rage.ecommerce.application.dto.item.CreateItemResponseDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.model.Item;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.infrastructure.adapter.out.ItemEntity;

public class ItemMapper {


    public static ItemEntity toEntity(Item item) {
        ItemEntity entity = new ItemEntity();
        entity.setItemId(item.getItemId());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setPrice(item.getPrice());

        return entity;
    }

    public static Item toDomain(ItemEntity entity) {
        return Item.builder()
                .itemId(entity.getItemId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .build();
    }

    public static Item toDomain(CreateItemRequestDTO dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }

    public static CreateItemResponseDTO toCreateItemResponseDTO(Item item) {
        return CreateItemResponseDTO.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .build();
    }

}
