package com.rage.ecommerce.infrastructure.adapter.out;


import com.rage.ecommerce.domain.enums.OrderState;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    private UUID id;
    private OrderState orderState;
}
