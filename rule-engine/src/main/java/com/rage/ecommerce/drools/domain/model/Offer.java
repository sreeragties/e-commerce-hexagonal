package com.rage.ecommerce.drools.domain.model;

import com.rage.ecommerce.drools.domain.model.enums.CustomerSubscription;
import com.rage.ecommerce.drools.domain.model.enums.ItemOfferLevel;
import com.rage.ecommerce.drools.domain.model.enums.OrderState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class Offer {

    private UUID processId;
    private OrderState orderState;
    private LocalDate dateOfBirth;
    private CustomerSubscription subscription;
    private ItemOfferLevel itemOfferLevel;
    private String result;
}
