package com.shop.ShopBE.model;

import java.math.BigDecimal;

public class OrderItem  {
    private Long OrderId;
    private Long itemId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem() {}

    public Long getOrderId() {
        return this.OrderId;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setOrderId(Long orderId) {
        this.OrderId = orderId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
