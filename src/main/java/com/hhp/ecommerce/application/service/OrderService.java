package com.hhp.ecommerce.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderItem;
import com.hhp.ecommerce.infra.persistence.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public Order createOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems().stream()
                .map(itemRequest -> {
                    productService.checkStockAvailability(itemRequest.getProductId(), itemRequest.getQuantity());
                    int price = productService.getProductPrice(itemRequest.getProductId());
                    return OrderItem.create(itemRequest.getProductId(), itemRequest.getQuantity(), price);
                })
                .toList();

        Order newOrder = Order.create(order.getUserId(), orderItems);

        Order savedOrder = orderRepository.save(newOrder);
        if (savedOrder.getId() == null) {
            throw new IllegalStateException("주문이 데이터베이스에 저장되지 않았습니다.");
        }

        return savedOrder;
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. ID: " + orderId));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
