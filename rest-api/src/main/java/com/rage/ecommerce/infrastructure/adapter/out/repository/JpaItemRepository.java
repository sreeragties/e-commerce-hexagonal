package com.rage.ecommerce.infrastructure.adapter.out.repository;

import com.rage.ecommerce.infrastructure.adapter.out.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaItemRepository extends JpaRepository<ItemEntity, UUID> {
}
