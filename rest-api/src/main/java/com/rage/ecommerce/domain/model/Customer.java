package com.rage.ecommerce.domain.model;

import com.rage.ecommerce.domain.enums.CustomerSubscription;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class Customer {

    private UUID customerId;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private CustomerSubscription subscription;
}