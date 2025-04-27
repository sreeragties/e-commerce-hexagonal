package com.rage.ecommerce.application.dto.item;

import com.rage.ecommerce.domain.enums.ItemOfferLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemRequestDTO {

    private String name;
    private String description;
    private double price;
    private ItemOfferLevel itemOfferLevel;
}
