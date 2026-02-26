package com.shop.ShopBE.service;

import com.shop.ShopBE.dto.order.OrderDetailsResponse;
import com.shop.ShopBE.dto.order.OrderItemResponse;
import com.shop.ShopBE.dto.order.OrderResponse;
import com.shop.ShopBE.exception.BadRequestException;
import com.shop.ShopBE.exception.NotFoundException;
import com.shop.ShopBE.model.Item;
import com.shop.ShopBE.model.Order;
import com.shop.ShopBE.model.OrderItem;
import com.shop.ShopBE.model.OrderStatus;
import com.shop.ShopBE.model.User;
import com.shop.ShopBE.repository.ItemRepository;
import com.shop.ShopBE.repository.OrderItemRepository;
import com.shop.ShopBE.repository.OrderRepository;
import com.shop.ShopBE.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<OrderResponse> getOrdersForUser(long userId) {
        return orderRepository.findAllByUser(userId).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    public OrderDetailsResponse getOrderDetails(long userId, long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        if (order.getUserId() != userId) {
            throw new BadRequestException("Order does not belong to this user");
        }

        return buildOrderDetails(order);
    }

    public OrderDetailsResponse getPendingOrder(long userId) {
        return orderRepository.findTempByUser(userId)
                .map(this::buildOrderDetails)
                .orElse(null);
    }

    @Transactional
    public OrderDetailsResponse changePendingItemQuantity(long userId, long itemId, int quantityChange) {
        if (quantityChange == 0) {
            throw new BadRequestException("quantityChange cannot be 0");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));

        Order tempOrder = orderRepository.findTempByUser(userId)
                .orElseGet(() -> createTempOrderForUser(userId));

        if (tempOrder.getStatus() != OrderStatus.TEMP) {
            throw new BadRequestException("No TEMP order exists for this user");
        }

        if (quantityChange > 0) {
            int quantityToAdd = quantityChange;

            // reduce stock first
            int updated = itemRepository.decreaseStock(itemId, quantityToAdd);
            if (updated == 0) {
                throw new BadRequestException("Not enough stock for item: " + itemId);
            }

            orderItemRepository.upsertIncreaseQty(
                    tempOrder.getId(),
                    itemId,
                    quantityToAdd,
                    item.getPriceUsd()
            );

        } else {
            int quantityToRemove = Math.abs(quantityChange);

            Integer currentQty = orderItemRepository.getQuantity(tempOrder.getId(), itemId);
            if (currentQty == null) {
                throw new BadRequestException("Item is not in the order");
            }
            if (quantityToRemove > currentQty) {
                throw new BadRequestException("Cannot remove more than existing quantity");
            }

            orderItemRepository.decreaseQty(tempOrder.getId(), itemId, quantityToRemove);

            Integer newQty = orderItemRepository.getQuantity(tempOrder.getId(), itemId);
            if (newQty != null && newQty <= 0) {
                orderItemRepository.removeItem(tempOrder.getId(), itemId);
            }

            itemRepository.increaseStock(itemId, quantityToRemove);
        }

        List<OrderItem> itemsInOrder = orderItemRepository.findByOrderId(tempOrder.getId());
        if (itemsInOrder.isEmpty()) {
            orderRepository.delete(tempOrder.getId());
            return null;
        }

        BigDecimal total = calculateTotal(itemsInOrder);
        orderRepository.updateTotal(tempOrder.getId(), total);

        Order refreshed = orderRepository.findById(tempOrder.getId())
                .orElseThrow(() -> new NotFoundException("Order not found after update"));

        return buildOrderDetails(refreshed);
    }

    @Transactional
    public OrderDetailsResponse payPendingOrder(long userId) {
        Order tempOrder = orderRepository.findTempByUser(userId)
                .orElseThrow(() -> new BadRequestException("No TEMP order to pay"));

        List<OrderItem> items = orderItemRepository.findByOrderId(tempOrder.getId());
        if (items.isEmpty()) {
            orderRepository.delete(tempOrder.getId());
            throw new BadRequestException("TEMP order is empty");
        }

        BigDecimal total = calculateTotal(items);
        orderRepository.close(tempOrder.getId(), total);

        Order closed = orderRepository.findById(tempOrder.getId())
                .orElseThrow(() -> new NotFoundException("Order not found after payment"));

        return buildOrderDetails(closed);
    }

    private Order createTempOrderForUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        long orderId = orderRepository.createTemp(userId, user.getCountry(), user.getCity());

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found after creation"));
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem oi : items) {
            BigDecimal line = oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
            total = total.add(line);
        }
        return total;
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse resp = new OrderResponse();
        resp.id = order.getId();
        resp.createdAt = order.getCreatedAt();
        resp.shipCountry = order.getShipCountry();
        resp.shipCity = order.getShipCity();
        resp.totalPrice = order.getTotalPrice();
        resp.status = order.getStatus();
        return resp;
    }

    private OrderDetailsResponse buildOrderDetails(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> itemResponses = orderItems.stream().map(oi -> {
            Item item = itemRepository.findById(oi.getItemId())
                    .orElseThrow(() -> new NotFoundException("Item not found: " + oi.getItemId()));

            OrderItemResponse resp = new OrderItemResponse();
            resp.itemId = item.getId();
            resp.title = item.getTitle();
            resp.imageUrl = item.getImageUrl();
            resp.unitPrice = oi.getUnitPrice();
            resp.quantity = oi.getQuantity();
            resp.lineTotal = oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
            return resp;
        }).toList();

        OrderDetailsResponse resp = new OrderDetailsResponse();
        resp.id = order.getId();
        resp.createdAt = order.getCreatedAt();
        resp.shipCountry = order.getShipCountry();
        resp.shipCity = order.getShipCity();
        resp.totalPrice = order.getTotalPrice();
        resp.status = order.getStatus();
        resp.items = itemResponses;
        return resp;
    }
}