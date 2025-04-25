package com.rage.ecommerce.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.application.dto.customer.CreateCustomerRequestDTO;
import com.rage.ecommerce.application.dto.customer.CreateCustomerResponseDTO;
import com.rage.ecommerce.application.mapper.CustomerMapper;
import com.rage.ecommerce.domain.port.in.CustomerService;
import com.rage.ecommerce.domain.port.out.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    @Override
    public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO createCustomerRequestDTO) throws JsonProcessingException {
        var response = customerRepository.save(customerMapper.toDomain(createCustomerRequestDTO));
        return customerMapper.toCreateCustomerResponseDTO(response);
    }
}
