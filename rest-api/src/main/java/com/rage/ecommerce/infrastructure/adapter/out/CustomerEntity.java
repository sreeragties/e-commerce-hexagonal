package com.rage.ecommerce.infrastructure.adapter.out;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "customer_id", updatable = false, nullable = false)
    private UUID customerId;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_email")
    private String email;
}
