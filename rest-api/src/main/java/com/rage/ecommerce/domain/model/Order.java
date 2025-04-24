package com.rage.ecommerce.domain.model;

import com.rage.ecommerce.domain.enums.OrderState;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Order {

    private UUID processId;
    private OrderState orderState;
    private UUID itemId;
    private UUID customerId;

}
