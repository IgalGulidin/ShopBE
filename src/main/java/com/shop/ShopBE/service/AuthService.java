package com.shop.ShopBE.service;

import com.shop.ShopBE.dto.auth.LoginRequest;
import com.shop.ShopBE.dto.auth.LoginResponse;
import com.shop.ShopBE.dto.auth.SignupRequest;
import com.shop.ShopBE.exception.BadRequestException;
import com.shop.ShopBE.exception.UnauthorizedException;
import com.shop.ShopBE.mapper.UserMapper;
import com.shop.ShopBE.model.User;
import com.shop.ShopBE.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;


@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final TokenStore tokenStore;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, TokenStore tokenStore, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
        this.userMapper = userMapper;
    }
    
    public LoginResponse signUp(SignupRequest req) {
        userRepository.findByEmail(req.email).ifPresent(user -> {
            throw new BadRequestException("Email already exists");
        });
        
        User user = new User();
        user.setFirstName(req.firstName);
        user.setLastName(req.lastName);
        user.setEmail(req.email);
        user.setPasswordHash(hash(req.password));
        user.setPhone(req.phone);
        user.setCountry(req.country);
        user.setCity(req.city);

        long id = userRepository.create(user);
        user.setId(id);

        String token = tokenStore.issueToken(id);
        return new LoginResponse(token, userMapper.toResponse(user));
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!user.getPasswordHash().equals(hash(req.password))) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = tokenStore.issueToken(user.getId());
        return new LoginResponse(token, userMapper.toResponse(user));
    }

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
