package com.shop.ShopBE.mapper;

import com.shop.ShopBE.dto.user.UserResponse;
import com.shop.ShopBE.model.User;

public class UserMapper {
    public UserResponse toResponse(User user) {
        UserResponse res = new UserResponse();
        res.id = user.getId();
        res.firstName = user.getFirstName();
        res.lastName = user.getLastName();
        res.email = user.getEmail();
        res.phone = user.getPhone();
        res.country = user.getCountry();
        res.city = user.getCity();
        return res;
    }
}
