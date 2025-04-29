package com.rage.ecommerce.infrastructure.adapter.out;


import com.rage.ecommerce.domain.enums.OrderState;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID processId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState orderState;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;

    @Column(name = "calculated_price")
    private double calculatedPrice;

    @Column(name = "offer_reason")
    private String reason;

    @Column(name = "offer_rate")
    private Double offerRate;
}
