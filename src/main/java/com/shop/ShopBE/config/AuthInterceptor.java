package com.shop.ShopBE.config;

import com.shop.ShopBE.exception.UnauthorizedException;
import com.shop.ShopBE.service.TokenStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenStore tokenStore;

    public AuthInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing Authorization header");
        }

        String token = header.substring("Bearer ".length()).trim();
        Long userId = tokenStore.resolveUserId(token);
        if (userId == null) {
            throw new UnauthorizedException("Invalid token");
        }

        request.setAttribute("userId", userId);
        return true;
    }
}
