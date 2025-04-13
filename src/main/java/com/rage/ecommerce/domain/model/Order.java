package com.rage.ecommerce.domain.model;

import com.rage.ecommerce.domain.enums.OrderState;
import lombok.Data;

import java.util.UUID;

@Data
public class Order {

    private UUID id;
    private OrderState orderState;

}
