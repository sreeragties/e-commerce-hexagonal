package com.rage.ecommerce.ship.dto;

import com.rage.ecommerce.ship.model.OrderState
import java.util.*

class StageForDeliverResponseDTO {

    var processId: UUID? = null
    var orderState: OrderState? = null
}
