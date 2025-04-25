package com.rage.ecommerce.infrastructure.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.customer.CreateCustomerRequestDTO;
import com.rage.ecommerce.application.dto.order.ErrorResponseDTO;
import com.rage.ecommerce.domain.port.in.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customers/create")
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerRequestDTO createCustomerRequestDTO) throws JsonProcessingException {
        var item = customerService.createCustomer(createCustomerRequestDTO);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("Failed to create item. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
