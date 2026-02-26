package com.shop.ShopBE.repository.mapper;

import com.shop.ShopBE.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(rs.getLong("order_id"));
        orderItem.setItemId(rs.getLong("item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));
        return orderItem;
    }
}
