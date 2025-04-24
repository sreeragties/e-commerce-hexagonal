package com.rage.ecommerce.application.dto.item;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateItemResponseDTO {

    private UUID itemId;
    private String name;
}
