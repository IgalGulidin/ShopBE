package com.shop.ShopBE.service;


import com.shop.ShopBE.dto.item.ItemResponse;
import com.shop.ShopBE.exception.BadRequestException;
import com.shop.ShopBE.exception.NotFoundException;
import com.shop.ShopBE.mapper.ItemMapper;
import com.shop.ShopBE.repository.FavoriteRepository;
import com.shop.ShopBE.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public FavoriteService(FavoriteRepository favoriteRepository, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.favoriteRepository = favoriteRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public List<ItemResponse> getFavorites(long userId) {
        List<Long> itemsIds = favoriteRepository.findItemIdsByUser(userId);

        return itemsIds.stream()
                .map(id -> itemRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Item not found: " + id)))
                .map(itemMapper::toResponse)
                .toList();
    }

    public void addFavorite(long userId, long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        favoriteRepository.add(userId, itemId);
    }

    public void removeFavorite(long userId, long itemId) {
        int rows = favoriteRepository.remove(userId, itemId);
        if (rows == 0) {
            throw new BadRequestException("Item is not in favorites");
        }
    }
}
