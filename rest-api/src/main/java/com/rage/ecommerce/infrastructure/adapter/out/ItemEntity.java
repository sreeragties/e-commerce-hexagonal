package com.rage.ecommerce.infrastructure.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private UUID itemId;

    private String name;
    private String description;
    private double price;
}
