package com.rage.ecommerce.application.dto.order;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateOrderRequestDTO {

    private UUID itemId;
    private UUID customerId;

}
