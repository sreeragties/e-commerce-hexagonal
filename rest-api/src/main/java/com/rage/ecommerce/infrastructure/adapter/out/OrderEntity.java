package com.rage.ecommerce.infrastructure.adapter.out;


import com.rage.ecommerce.domain.enums.OrderState;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID processId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState orderState;

    @Column
    private String itemName;

    @Column
    private String customerName;
}
