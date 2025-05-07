package com.rage.ecommerce.application.dto.order;

import com.rage.ecommerce.domain.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverOrderRequestDTO {

    private UUID processId;
    private OrderState orderState;
}
