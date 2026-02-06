package com.shop.ShopBE.dto.item;

import java.math.BigDecimal;

public class ItemResponse {
    public Long id;
    public String title;
    public String imageUrl;
    public BigDecimal priceUsd;
    public int stockQty;
}
