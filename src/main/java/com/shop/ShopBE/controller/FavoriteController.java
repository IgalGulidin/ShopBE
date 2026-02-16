package com.shop.ShopBE.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @GetMapping
    public List<Long> getFavorites(@RequestAttribute("userId") long userId) {
        return List.of();
    }
}
