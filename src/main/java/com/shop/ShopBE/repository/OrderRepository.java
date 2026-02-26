package com.shop.ShopBE.repository;

import com.shop.ShopBE.model.Order;
import com.shop.ShopBE.model.OrderStatus;
import com.shop.ShopBE.repository.mapper.OrderRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Order> findById(long orderId) {
        List<Order> list = jdbcTemplate.query("SELECT * FROM orders WHERE id = ?", new OrderRowMapper(), orderId);
        return list.stream().findFirst();
    }

    public List<Order> findAllByUser(long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE user_id = ? ORDER BY (status='TEMP') DESC, created_at DESC",
                new OrderRowMapper(),
                userId
        );
    }

    public Optional<Order> findTempByUser(long userId) {
        List<Order> list = jdbcTemplate.query(
                "SELECT * FROM orders WHERE user_id = ? AND STATUS = 'TEMP' LIMIT 1",
                new OrderRowMapper(),
                userId
        );
        return list.stream().findFirst();
    }

    public long createTemp(long userId, String shipCountry, String shipCity) {
        jdbcTemplate.update(
                "INSERT INTO orders(user_id, created_at, ship_country, ship_city, total_price, status) VALUES(?, ?, ?, ?, ?, ?)",
                userId,
                Timestamp.valueOf(LocalDateTime.now()),
                shipCountry,
                shipCity,
                BigDecimal.ZERO,
                OrderStatus.TEMP.name()
        );

        Long id = jdbcTemplate.queryForObject(
                "SELECT id FROM orders WHERE user_id = ? AND STATUS = 'TEMP' ORDER BY created_at DESC LIMIT 1",
                Long.class,
                userId
        );
        return id == null ? -1 : id;
    }

    public void updateTotal(long orderId, BigDecimal total) {
        jdbcTemplate.update("UPDATE orders SET total_price = ? WHERE id = ?", total, orderId);
    }

    public void close(long orderId, BigDecimal finalTotal) {
        jdbcTemplate.update("UPDATE orders SET status = 'CLOSE', total_price = ? WHERE id = ?", finalTotal, orderId);
    }

    public int delete(long orderId) {
        return jdbcTemplate.update("DELETE FROM orders WHERE id = ?", orderId);
    }
}
