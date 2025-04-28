package com.rage.ecommerce.drools.application.dto;

import com.rage.ecommerce.drools.domain.model.enums.CustomerSubscription;
import com.rage.ecommerce.drools.domain.model.enums.ItemOfferLevel;
import com.rage.ecommerce.drools.domain.model.enums.OrderState;
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
public class CheckOfferResponseDTO {

    private UUID processId;
    private OrderState orderState;
    private UUID itemId;
    private UUID customerId;
    private LocalDate dateOfBirth;
    private CustomerSubscription subscription;
    private ItemOfferLevel itemOfferLevel;
}
