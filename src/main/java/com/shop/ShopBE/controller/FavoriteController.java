package com.shop.ShopBE.controller;


import com.shop.ShopBE.dto.item.ItemResponse;
import com.shop.ShopBE.repository.FavoriteRepository;
import com.shop.ShopBE.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ItemResponse> getFavorites(@RequestAttribute("userId") long userId) {
        return service.getFavorites(userId);
    }

    @PostMapping("/{itemId}")
    public void addFavorite(@RequestAttribute("userId") long userId, @PathVariable long itemId) {
        service.addFavorite(userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void removeFavorite(@RequestAttribute("userId") long userId, @PathVariable long itemId) {
        service.removeFavorite(userId, itemId);
    }
}
