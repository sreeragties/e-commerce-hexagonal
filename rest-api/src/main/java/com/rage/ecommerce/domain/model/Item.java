package com.rage.ecommerce.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Item {

    private UUID itemId;
    private String name;
    private String description;
    private double price;
}
