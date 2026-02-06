package com.shop.ShopBE.dto.auth;

public class LoginResponse {
    public String token;
    public Object user;

    public LoginResponse(String token, Object user) {
        this.token = token;
        this.user = user;
    }
}
