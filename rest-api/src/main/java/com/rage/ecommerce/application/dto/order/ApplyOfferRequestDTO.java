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
public class ApplyOfferRequestDTO {

    private UUID processId;
    private Double offerRate;
    private String reason;
}
