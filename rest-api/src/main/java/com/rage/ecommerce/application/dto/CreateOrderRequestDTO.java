package com.rage.ecommerce.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequestDTO {

    private String itemId;
    private String customerId;

}
