package com.shop.ShopBE.repository;

import com.shop.ShopBE.model.OrderItem;
import com.shop.ShopBE.repository.mapper.OrderItemRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderItem> findByOrderId(long orderId) {
        return jdbcTemplate.query("SELECT * FROM order_items WHERE order_id = ? ORDER BY item_id",
                new OrderItemRowMapper(),
                orderId);
    }

    public int upsertIncreaseQty(long orderId, long itemId, int quantityChange, java.math.BigDecimal unitPrice) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM order_items WHERE order_id = ? AND item_id = ?",
                Integer.class,
                orderId, itemId
        );
        if (count != null && count > 0) {
            return jdbcTemplate.update(
                    "UPDATE order_items SET quantity = quantity + ? WHERE order_id = ? AND item_id = ?",
                    quantityChange, orderId, itemId
            );
        }
        return jdbcTemplate.update(
                "INSERT INTO order_items(order_id, item_id, quantity, unit_price) VALUES(?, ?, ?, ?)",
                orderId, itemId, quantityChange, unitPrice
        );
    }

    public int decreaseQty(long orderId, long itemId, int quantityToRemove) {
        return jdbcTemplate.update(
                "UPDATE order_items SET quantity = quantity - ? WHERE order_id = ? AND item_id = ? AND quantity >= ?",
                quantityToRemove, orderId, itemId, quantityToRemove
        );
    }

    public int removeItem(long orderId, long itemId) {
        return jdbcTemplate.update(
                "DELETE FROM order_items WHERE order_id = ? AND item_id = ?", orderId, itemId);
    }

    public int deleteAllByOrder(long orderId) {
        return jdbcTemplate.update("DELETE FROM order_items WHERE order_id = ?", orderId);
    }

    public Integer getQuantity(long orderId, long itemId) {
        List<Integer> list = jdbcTemplate.query(
                "SELECT quantity FROM order_items WHERE order_id = ? AND item_id = ?",
                (rs, rowNum) -> rs.getInt("quantity"),
                orderId, itemId
        );
        return list.isEmpty() ? null : list.get(0);
    }

}
