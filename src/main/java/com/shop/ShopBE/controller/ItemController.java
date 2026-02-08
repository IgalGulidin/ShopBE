package com.shop.ShopBE.controller;


import com.shop.ShopBE.dto.item.ItemResponse;
import com.shop.ShopBE.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemResponse> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/search")
    public List<ItemResponse> search(@RequestParam String query) {
        return itemService.search(query);
    }

    @GetMapping("/{id}")
    public ItemResponse getById(@PathVariable long id) {
        return itemService.getById(id);
    }
}
