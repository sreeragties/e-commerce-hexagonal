package com.rage.ecommerce.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.customer.CreateCustomerRequestDTO;
import com.rage.ecommerce.application.dto.customer.CreateCustomerResponseDTO;
import jakarta.transaction.Transactional;

public interface CustomerService {


    @Transactional
    CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO createCustomerRequestDTO) throws JsonProcessingException;
}
