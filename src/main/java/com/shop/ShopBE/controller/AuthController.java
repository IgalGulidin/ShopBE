package com.shop.ShopBE.controller;


import com.shop.ShopBE.dto.auth.LoginRequest;
import com.shop.ShopBE.dto.auth.LoginResponse;
import com.shop.ShopBE.dto.auth.SignupRequest;
import com.shop.ShopBE.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public LoginResponse signup(@Valid @RequestBody SignupRequest req) {
        return authService.signUp(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
