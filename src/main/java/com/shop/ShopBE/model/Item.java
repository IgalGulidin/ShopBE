package com.shop.ShopBE.model;

import java.math.BigDecimal;

public class Item {
    private Long id;
    private String title;
    private String imageUrl;
    private BigDecimal priceUsd;
    private int stockQty;

    public Item() {}

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public BigDecimal getPrice() {
        return this.priceUsd;
    }

    public int getStockQty() {
        return this.stockQty;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPriceUsd(BigDecimal price) {
        this.priceUsd = price;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }
}
