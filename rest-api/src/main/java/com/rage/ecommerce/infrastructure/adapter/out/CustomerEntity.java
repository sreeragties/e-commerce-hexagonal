package com.rage.ecommerce.infrastructure.adapter.out;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
