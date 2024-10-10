package com.hhp.ecommerce.interfaces.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.interfaces.dto.OrderRequest;
import com.hhp.ecommerce.interfaces.dto.OrderResponse;

@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        int totalPrice = request.getItems().stream()
                .mapToInt(item -> item.getQuantity() * 1000)
                .sum();

        UUID orderId = UUID.randomUUID();

        OrderResponse response = new OrderResponse(orderId, request.getUserId(), totalPrice, "SUCCESS");
        return ResponseEntity.ok(response);
    }
}