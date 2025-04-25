package com.rage.ecommerce.infrastructure.adapter.out;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "items")
@Data
@Builder
public class ItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "item_id", updatable = false, nullable = false)
    private UUID itemId;

    private String name;
    private String description;
    private double price;
}
