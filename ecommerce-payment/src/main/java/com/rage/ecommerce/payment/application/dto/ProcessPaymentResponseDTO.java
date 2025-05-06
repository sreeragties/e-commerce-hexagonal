package com.rage.ecommerce.payment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentResponseDTO {

    private UUID processId;
    private UUID itemId;
    private UUID customerId;
    private Double calculatedPrice;
}
