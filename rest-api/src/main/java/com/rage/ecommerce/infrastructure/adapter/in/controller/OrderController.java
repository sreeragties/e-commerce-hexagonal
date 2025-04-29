package com.rage.ecommerce.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.order.CheckOfferResponseDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.domain.port.in.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/create")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException {
        var order = orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders/{orderId}/check-offer")
    public ResponseEntity<CheckOfferResponseDTO> checkOffer(@PathVariable UUID orderId) throws JsonProcessingException {
        var response = orderService.checkOffer(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/orders/{orderId}/cancel-offer")
    public ResponseEntity<String> cancelOffer(@PathVariable UUID orderId) {
        if (orderService.cancelOffer(orderId)) {
            return ResponseEntity.ok("Offer cancelled successfully, now in PAYMENT_PENDING state.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel offer.");
        }
    }

    @PostMapping("/orders/{orderId}/place")
    public ResponseEntity<String> placeOrder(@PathVariable UUID orderId) {
        if (orderService.placeOrder(orderId)) {
            return ResponseEntity.ok("Order placed successfully, now in PAYMENT_PENDING state.");
        } else {
            return ResponseEntity.badRequest().body("Failed to place order.");
        }
    }
}
