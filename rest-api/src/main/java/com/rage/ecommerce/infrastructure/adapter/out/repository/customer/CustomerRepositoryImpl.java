package com.rage.ecommerce.infrastructure.adapter.out.repository.customer;

import com.rage.ecommerce.application.mapper.CustomerMapper;
import com.rage.ecommerce.domain.model.Customer;
import com.rage.ecommerce.domain.port.out.repository.CustomerRepository;
import com.rage.ecommerce.infrastructure.adapter.out.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<Customer> findByItemId(UUID customerId) {
        CustomerEntity customerEntity = jpaCustomerRepository.findById(customerId).orElseThrow();
        return Optional.of(customerMapper.toDomain(customerEntity));
    }

    @Override
    public Customer save(Customer item) {
        return customerMapper.toDomain(jpaCustomerRepository.save(customerMapper.toEntity(item)));
    }
}
