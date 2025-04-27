package com.rage.ecommerce.application.dto.order;

import com.rage.ecommerce.domain.enums.CustomerSubscription;
import com.rage.ecommerce.domain.enums.ItemOfferLevel;
import com.rage.ecommerce.domain.enums.OrderState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CheckOrderResponseDTO {

    private UUID processId;
    private OrderState orderState;
    private UUID itemId;
    private UUID customerId;
    private LocalDate dateOfBirth;
    private CustomerSubscription subscription;
    private ItemOfferLevel itemOfferLevel;
}
