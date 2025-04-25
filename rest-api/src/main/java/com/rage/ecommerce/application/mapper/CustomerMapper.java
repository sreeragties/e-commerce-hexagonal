package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.domain.model.Customer;
import com.rage.ecommerce.infrastructure.adapter.out.CustomerEntity;

public class CustomerMapper {

    public static CustomerEntity toEntity(Customer customer) {

        return CustomerEntity.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getCustomerEmail())
                .build();
    }

    public static Customer toDomain(CustomerEntity entity) {
        return Customer.builder()
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .customerEmail(entity.getCustomerEmail())
                .build();
    }
}
