package com.rage.ecommerce.application.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateCustomerResponseDTO {

    private UUID customerId;
    private String name;
}
