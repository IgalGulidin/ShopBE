package com.shop.ShopBE.dto.order;

import java.math.BigDecimal;

public class OrderItemResponse {
    public Long itemId;
    public String title;
    public String imageUrl;
    public BigDecimal unitPrice;
    public int quantity;
    public BigDecimal lineTotal;
}
