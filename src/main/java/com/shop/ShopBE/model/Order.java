package com.shop.ShopBE.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private String shipCountry;
    private String shipCity;
    private BigDecimal totalPrice;
    private OrderStatus status;

    public Order() {}

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public String getShipCountry() {
        return this.shipCountry;
    }

    public String getShipCity() {
        return this.shipCity;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
