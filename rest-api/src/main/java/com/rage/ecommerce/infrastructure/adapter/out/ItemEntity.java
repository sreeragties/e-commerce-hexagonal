package com.rage.ecommerce.infrastructure.adapter.out;

import com.rage.ecommerce.domain.enums.ItemOfferLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "item_id", updatable = false, nullable = false)
    private UUID itemId;

    private String name;
    private String description;
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_offer_level")
    private ItemOfferLevel itemOfferLevel;
}
