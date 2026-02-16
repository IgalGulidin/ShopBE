
package com.shop.ShopBE.repository;

import com.shop.ShopBE.model.User;
import com.shop.ShopBE.repository.mapper.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findById(long id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new UserRowMapper(), id);
        return list.stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new UserRowMapper(), email);
        return list.stream().findFirst();
    }

    public long create(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(first_name, last_name, email, password_hash, phone, country, city) VALUES(?, ?, ?, ?, ?, ?, ?)",
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getPhone(),
                user.getCountry(),
                user.getCity()
        );
        Long id = jdbcTemplate.queryForObject("SELECT id FROM users WHERE email = ?", Long.class, user.getEmail());
        return id == null ? -1 : id;
    }

    public int deleteUserById(long id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}
