package com.shop.ShopBE.controller;


import com.shop.ShopBE.dto.order.AddItemRequest;
import com.shop.ShopBE.dto.order.OrderDetailsResponse;
import com.shop.ShopBE.dto.order.OrderResponse;
import com.shop.ShopBE.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderResponse> myOrders(@RequestAttribute("userId") long userId) {
        return service.getOrdersForUser(userId);
    }

    @GetMapping("/pending")
    public OrderDetailsResponse pending(@RequestAttribute("userId") long userId) {
        return service.getPendingOrder(userId);
    }

    @GetMapping("/{orderId}")
    public OrderDetailsResponse details(@RequestAttribute("userId") long userId, @PathVariable long orderId) {
        return service.getOrderDetails(userId, orderId);
    }

    @PostMapping("/pending/items/{itemId}")
    public OrderDetailsResponse changeQty(@RequestAttribute("userId") long userId, @PathVariable long itemId, @Valid @RequestBody AddItemRequest addItemRequest) {
        return service.changePendingItemQuantity(userId, itemId, addItemRequest.quantityChange);
    }

    @PostMapping("/pending/pay")
    public OrderDetailsResponse pay(@RequestAttribute("userId") long userId) {
        return service.payPendingOrder(userId);
    }
}
