package com.hhp.ecommerce.interfaces.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private UUID userId;
    private int totalPrice;
    private String orderStatus;
}