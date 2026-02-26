package com.shop.ShopBE.service;

import com.shop.ShopBE.exception.BadRequestException;
import com.shop.ShopBE.exception.NotFoundException;
import com.shop.ShopBE.model.Order;
import com.shop.ShopBE.model.OrderItem;
import com.shop.ShopBE.model.OrderStatus;
import com.shop.ShopBE.repository.ItemRepository;
import com.shop.ShopBE.repository.OrderItemRepository;
import com.shop.ShopBE.repository.OrderRepository;
import com.shop.ShopBE.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    public UserService(UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void deleteMyAccount(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Order temp = orderRepository.findTempByUser(userId).orElse(null);
        if (temp != null && temp.getStatus() == OrderStatus.TEMP) {
            List<OrderItem> items = orderItemRepository.findByOrderId(temp.getId());
            for (OrderItem orderItem : items) {
                itemRepository.increaseStock(orderItem.getItemId(), orderItem.getQuantity());
            }
        }

        int deleted = userRepository.deleteUserById(userId);
        if (deleted == 0) {
            throw new BadRequestException("Failed to delete user");
        }
    }
}
