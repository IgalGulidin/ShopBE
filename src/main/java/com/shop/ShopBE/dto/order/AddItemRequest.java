package com.shop.ShopBE.dto.order;

import jakarta.validation.constraints.NotNull;

public class AddItemRequest {
    @NotNull
    public Integer quantityDelta;
}
