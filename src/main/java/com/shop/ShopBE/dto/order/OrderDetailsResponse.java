package com.shop.ShopBE.dto.order;

import com.shop.ShopBE.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsResponse {
    public Long id;
    public LocalDateTime createdAt;
    public String shipCountry;
    public String shipCity;
    public BigDecimal totalPrice;
    public OrderStatus status;
    public List<OrderItemResponse> items;
}
