package com.rage.ecommerce.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.customer.CreateCustomerRequestDTO;
import com.rage.ecommerce.application.dto.customer.CreateCustomerResponseDTO;
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
    public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@RequestBody CreateCustomerRequestDTO createCustomerRequestDTO) throws JsonProcessingException {
        var item = customerService.createCustomer(createCustomerRequestDTO);
        return ResponseEntity.ok(item);
    }
}
