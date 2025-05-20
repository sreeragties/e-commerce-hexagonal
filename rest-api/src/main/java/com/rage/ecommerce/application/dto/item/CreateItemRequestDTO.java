package com.rage.ecommerce.application.dto.item;

import com.rage.ecommerce.domain.enums.ItemOfferLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDTO {

    private String name;
    private String description;
    private double price;
    private ItemOfferLevel itemOfferLevel;
}
