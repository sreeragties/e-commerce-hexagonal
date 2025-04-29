package com.rage.ecommerce.application.dto.order;

import com.rage.ecommerce.domain.enums.CustomerSubscription;
import com.rage.ecommerce.domain.enums.ItemOfferLevel;
import com.rage.ecommerce.domain.enums.OrderState;
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
public class ApplyOfferResponseDTO {

    private UUID processId;
    private OrderState orderState;
    private UUID itemId;
    private UUID customerId;
    private LocalDate dateOfBirth;
    private CustomerSubscription subscription;
    private ItemOfferLevel itemOfferLevel;
    private Double offerRate;
    private String reason;
}
