package com.rage.ecommerce.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.item.CreateItemRequestDTO;
import com.rage.ecommerce.application.dto.item.CreateItemResponseDTO;
import jakarta.transaction.Transactional;

public interface ItemService {


    @Transactional
    CreateItemResponseDTO createItem(CreateItemRequestDTO createItemRequestDTO) throws JsonProcessingException;
}
