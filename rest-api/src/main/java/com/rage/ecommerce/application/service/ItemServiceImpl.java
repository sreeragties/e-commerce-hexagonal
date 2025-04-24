package com.rage.ecommerce.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rage.ecommerce.application.dto.item.CreateItemRequestDTO;
import com.rage.ecommerce.application.dto.item.CreateItemResponseDTO;
import com.rage.ecommerce.domain.model.Item;
import com.rage.ecommerce.domain.port.in.ItemService;
import com.rage.ecommerce.domain.port.out.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rage.ecommerce.application.mapper.ItemMapper.toCreateItemResponseDTO;
import static com.rage.ecommerce.application.mapper.ItemMapper.toDomain;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public CreateItemResponseDTO createItem(CreateItemRequestDTO createItemRequestDTO) throws JsonProcessingException {
        Item item = toDomain(createItemRequestDTO);
        var response = itemRepository.save(item);
        return toCreateItemResponseDTO(response);
    }
}
