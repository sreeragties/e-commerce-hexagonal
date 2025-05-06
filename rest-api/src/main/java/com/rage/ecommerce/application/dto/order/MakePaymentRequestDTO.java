package com.rage.ecommerce.application.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MakePaymentRequestDTO {

    private UUID processId;
    private UUID itemId;
    private UUID customerId;
    private Double calculatedPrice;
}
