package com.shop.ShopBE.repository;

import com.shop.ShopBE.model.Item;
import com.shop.ShopBE.repository.mapper.ItemRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class ItemRepository {

    private final JdbcTemplate jdbc;

    public ItemRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Item> findAll() {
        return jdbc.query("SELECT * FROM items ORDER BY id", new ItemRowMapper());
    }

    public Optional<Item> findById(long id) {
        List<Item> items = jdbc.query("SELECT * FROM items WHERE id = ?", new ItemRowMapper(), id);
        return items.stream().findFirst();
    }

    public List<Item> searchByTitle(String query) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        return jdbc.query(
                "SELECT * FROM items WHERE LOWER(title) LIKE ? ORDER BY id",
                new ItemRowMapper(),
                searchPattern
        );
    }

    public int decreaseStock(long itemId, int quantity) {
        return jdbc.update(
                "UPDATE items SET stock_qty = stock_qty - ? WHERE id  = ? AND stock_qty >= ?",
                quantity, itemId, quantity
        );
    }

    public int increaseStock(long itemId, int quantity) {
        return jdbc.update("UPDATE items SET stock_qty = stock_qty + ? WHERE id = ?", quantity, itemId);
    }
}
