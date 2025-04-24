package com.rage.ecommerce.application.dto.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemRequestDTO {

    private String name;
    private String description;
    private double price;
}
