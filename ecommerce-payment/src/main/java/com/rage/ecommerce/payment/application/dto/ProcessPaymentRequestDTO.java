package com.rage.ecommerce.payment.application.dto;

import com.rage.ecommerce.payment.domain.enums.CustomerSubscription;
import com.rage.ecommerce.payment.domain.enums.ItemOfferLevel;
import com.rage.ecommerce.payment.domain.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequestDTO {

    private UUID processId;
    private UUID itemId;
    private UUID customerId;
    private Double calculatedPrice;
}
