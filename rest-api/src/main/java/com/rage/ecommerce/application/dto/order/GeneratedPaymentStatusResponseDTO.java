package com.rage.ecommerce.application.dto.order;

import com.rage.ecommerce.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedPaymentStatusResponseDTO {

    private UUID processId;
    private UUID itemId;
    private UUID customerId;
    private Double calculatedPrice;
    private PaymentStatus paymentStatus;
}
