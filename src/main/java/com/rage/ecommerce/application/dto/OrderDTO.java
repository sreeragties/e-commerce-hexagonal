package com.rage.ecommerce.application.dto;

import com.rage.ecommerce.domain.enums.OrderState;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderDTO {

    private UUID id;
    private OrderState orderState;
}
