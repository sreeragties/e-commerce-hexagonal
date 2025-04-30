package com.rage.ecommerce.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.item.CreateItemRequestDTO;
import com.rage.ecommerce.application.dto.item.CreateItemResponseDTO;
import com.rage.ecommerce.application.dto.order.ErrorResponseDTO;
import com.rage.ecommerce.domain.port.in.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items/create")
    public ResponseEntity<CreateItemResponseDTO> createItem(@RequestBody CreateItemRequestDTO createItemRequestDTO) throws JsonProcessingException {
        var item = itemService.createItem(createItemRequestDTO);
        return ResponseEntity.ok(item);
    }
}
