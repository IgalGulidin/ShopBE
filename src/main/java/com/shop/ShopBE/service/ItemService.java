package com.shop.ShopBE.service;

import com.shop.ShopBE.dto.item.ItemResponse;
import com.shop.ShopBE.exception.NotFoundException;
import com.shop.ShopBE.mapper.ItemMapper;
import com.shop.ShopBE.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public List<ItemResponse> getAll() {
        return itemRepository.findAll().stream().map(itemMapper::toResponse).toList();
    }

    public List<ItemResponse> search(String query) {
        return itemRepository.searchByTitle(query).stream().map(itemMapper::toResponse).toList();
    }

    public ItemResponse getById(long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Item not found: " + id));
    }
}
