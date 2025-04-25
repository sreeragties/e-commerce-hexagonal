package com.rage.ecommerce.infrastructure.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@Builder
public class CustomerEntity {

    private UUID customerId;
    private String customerName;
    private String customerEmail;
}
