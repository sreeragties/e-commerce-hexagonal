package com.rage.ecommerce.drools.application.dto;

import com.rage.ecommerce.drools.domain.model.enums.CustomerSubscription;
import com.rage.ecommerce.drools.domain.model.enums.ItemOfferLevel;
import com.rage.ecommerce.drools.domain.model.enums.OrderState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class OfferEvaluationResponseDTO {

    private UUID processId;
    private Double offerRate;
    private String reason;
}
