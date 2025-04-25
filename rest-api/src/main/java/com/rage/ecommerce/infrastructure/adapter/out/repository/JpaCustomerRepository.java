package com.rage.ecommerce.infrastructure.adapter.out.repository;

import com.rage.ecommerce.infrastructure.adapter.out.CustomerEntity;
import com.rage.ecommerce.infrastructure.adapter.out.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, UUID> {
}
