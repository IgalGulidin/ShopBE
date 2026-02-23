package com.shop.ShopBE.repository;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FavoriteRepository {

    private final JdbcTemplate jdbcTemplate;

    public FavoriteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> findItemIdsByUser(long userId) {
        return jdbcTemplate.query(
                "SELECT item_id FROM favorites WHERE user_id = ? ORDER BY item_id",
                (rs, rowNum) -> rs.getLong("item_id"),
                userId
        );
    }

    public void add(long userId, long itemId) {
        try {
            jdbcTemplate.update("INSERT INTO favorites(user_id, item_id) VALUES (?, ?)", userId, itemId);
        } catch (DuplicateKeyException ignored) {
        }
    }

    public int remove(long userId, long itemId) {
        return jdbcTemplate.update("DELETE FROM favorites WHERE user_id = ? AND item_id = ?", userId, itemId);
    }

    public void deleteAllFromUser(long userId) {
        jdbcTemplate.update("DELETE FROM favorites WHERE user_id = ?", userId);
    }
}
