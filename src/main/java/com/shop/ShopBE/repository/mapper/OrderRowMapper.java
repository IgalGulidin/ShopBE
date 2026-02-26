package com.shop.ShopBE.repository.mapper;

import com.shop.ShopBE.model.Order;
import com.shop.ShopBE.model.OrderStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));

        Timestamp ts = rs.getTimestamp("created_at");
        order.setCreatedAt(ts.toLocalDateTime());

        order.setShipCountry(rs.getString("ship_country"));
        order.setShipCity(rs.getString("ship_city"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        return order;
    }
}
