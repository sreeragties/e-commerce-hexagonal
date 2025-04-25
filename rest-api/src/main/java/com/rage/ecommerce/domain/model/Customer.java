package com.rage.ecommerce.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Customer {

    private UUID customerId;
    private String customerName;
    private String customerEmail;

}
