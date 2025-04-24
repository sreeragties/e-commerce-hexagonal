package com.rage.ecommerce.infrastructure.adapter.out.repository;

import com.rage.ecommerce.application.mapper.ItemMapper;
import com.rage.ecommerce.domain.model.Item;
import com.rage.ecommerce.domain.port.out.repository.ItemRepository;
import com.rage.ecommerce.infrastructure.adapter.out.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final JpaItemRepository jpaItemRepository;

    @Override
    public Optional<Item> findByItemId(UUID itemId) {
        ItemEntity itemEntity = jpaItemRepository.findById(itemId).orElseThrow();
        return Optional.of(ItemMapper.toDomain(itemEntity));
    }

    @Override
    public Item save(Item item) {
        return ItemMapper.toDomain(jpaItemRepository.save(ItemMapper.toEntity(item)));
    }
}
