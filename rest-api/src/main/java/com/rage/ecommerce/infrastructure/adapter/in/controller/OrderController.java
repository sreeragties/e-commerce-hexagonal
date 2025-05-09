package com.rage.ecommerce.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.port.in.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException {
        var order = orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/cancel-offer")
    public ResponseEntity<String> cancelOffer(@PathVariable UUID orderId) {
        if (orderService.cancelOffer(orderId)) {
            return ResponseEntity.ok("Offer cancelled successfully, now in PAYMENT_PENDING state.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel offer.");
        }
    }
}
