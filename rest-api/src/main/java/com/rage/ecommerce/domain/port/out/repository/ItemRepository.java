package com.rage.ecommerce.domain.port.out.repository;

import com.rage.ecommerce.domain.model.Item;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository {

    Optional<Item> findByItemId(UUID itemId);

    Item save(Item item);
}
