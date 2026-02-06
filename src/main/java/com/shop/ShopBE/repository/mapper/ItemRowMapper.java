package com.shop.ShopBE.repository.mapper;

import com.shop.ShopBE.model.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRowMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item i = new Item();
        i.setId(rs.getLong("id"));
        i.setTitle(rs.getString("title"));
        i.setImageUrl(rs.getString("image_url"));
        i.setPriceUsd(rs.getBigDecimal("price_usd"));
        i.setStockQty(rs.getInt("stock_qty"));
        return i;
    }
}
