package com.shop.ShopBE.mapper;

import com.shop.ShopBE.dto.item.ItemResponse;
import com.shop.ShopBE.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public ItemResponse toResponse(Item i) {
        ItemResponse res = new ItemResponse();
        res.id = i.getId();
        res.title = i.getTitle();
        res.imageUrl = i.getImageUrl();
        res.priceUsd = i.getPriceUsd();
        res.stockQty = i.getStockQty();
        return res;
    }
}
