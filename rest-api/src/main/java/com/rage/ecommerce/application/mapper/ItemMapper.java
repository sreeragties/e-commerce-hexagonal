package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.item.CreateItemRequestDTO;
import com.rage.ecommerce.application.dto.item.CreateItemResponseDTO;
import com.rage.ecommerce.domain.model.Item;
import com.rage.ecommerce.infrastructure.adapter.out.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemEntity toEntity(Item item);

    Item toDomain(ItemEntity entity);

    Item toDomain(CreateItemRequestDTO dto);

    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "name", target = "name")
    CreateItemResponseDTO toCreateItemResponseDTO(Item item);
}