package com.rage.ecommerce.domain.port.out.repository;

import com.rage.ecommerce.domain.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findByCustomerId(UUID customerId);

    Customer save(Customer customer);
}
