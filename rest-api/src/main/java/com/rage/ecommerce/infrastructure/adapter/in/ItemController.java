package com.rage.ecommerce.infrastructure.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.item.CreateItemRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.ErrorResponseDTO;
import com.rage.ecommerce.domain.port.in.ItemService;
import com.rage.ecommerce.domain.port.in.OrderService;
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
    public ResponseEntity<?> createOrder(@RequestBody CreateItemRequestDTO createItemRequestDTO) throws JsonProcessingException {
        var item = itemService.createItem(createItemRequestDTO);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("Failed to create item. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
