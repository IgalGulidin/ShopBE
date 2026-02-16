package com.shop.ShopBE.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, Long> tokenToUserId = new ConcurrentHashMap<>();

    public String issueToken(long userId) {
        String token = UUID.randomUUID().toString();
        tokenToUserId.put(token, userId);
        return token;
    }

    public Long resolveUserId(String token) {
        return tokenToUserId.get(token);
    }

    public void revokeToken(String token) {
        tokenToUserId.remove(token);
    }

    public void revokeAllForUser(long userId) {
        tokenToUserId.entrySet().removeIf(entry -> entry.getValue().equals(userId));
    }
}
