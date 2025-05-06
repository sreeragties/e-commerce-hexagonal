package com.rage.ecommerce.domain.model;

import com.rage.ecommerce.domain.enums.OrderState;
import com.rage.ecommerce.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class Order {

    private UUID processId;
    private OrderState orderState;
    private UUID itemId;
    private UUID customerId;
    private double calculatedPrice;
    private Double offerRate;
    private String reason;
    private PaymentStatus paymentStatus;
    private Instant requestCreatedDate;

}
