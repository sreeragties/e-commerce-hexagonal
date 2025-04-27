package com.rage.ecommerce.infrastructure.adapter.out;

import com.rage.ecommerce.domain.enums.CustomerSubscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription")
    private CustomerSubscription subscription;

    @Column(name = "dob")
    private LocalDate dateOfBirth;
}
