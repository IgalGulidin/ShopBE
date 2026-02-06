package com.shop.ShopBE.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {
    @NotBlank public String firstName;
    @NotBlank public String lastName;
    @Email @NotBlank public String email;
    @NotBlank public String password;
    public String phone;
    @NotBlank public String country;
    @NotBlank public String city;
}
