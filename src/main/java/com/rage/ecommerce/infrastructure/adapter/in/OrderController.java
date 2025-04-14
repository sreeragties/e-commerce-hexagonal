package com.rage.ecommerce.infrastructure.adapter.in;

import com.rage.ecommerce.domain.port.in.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/create")
    public ResponseEntity<String> createOrder() {
        if (orderService.createOrder() != null) {
            return ResponseEntity.ok("Order created successfully, now in CREATED state.");
        } else {
            return ResponseEntity.badRequest().body("Failed to place order.");
        }
    }

    @PostMapping("/orders/{orderId}/check-offer")
    public ResponseEntity<String> checkOffer(@PathVariable UUID orderId) {
        if (orderService.checkOffer(orderId)) {
            return ResponseEntity.ok("Order created successfully, now in OFFER_CHECKING state.");
        } else {
            return ResponseEntity.badRequest().body("Failed to check for offers.");
        }
    }

    @PostMapping("/orders/{orderId}/apply-offer")
    public ResponseEntity<String> applyOffer(@PathVariable UUID orderId) {
        if (orderService.applyOffer(orderId)) {
            return ResponseEntity.ok("Offer applied successfully, now in OFFER_APPLIED state.");
        } else {
            return ResponseEntity.badRequest().body("Failed to apply offer.");
        }
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
