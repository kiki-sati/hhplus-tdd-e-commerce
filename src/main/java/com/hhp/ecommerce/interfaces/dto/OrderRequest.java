package com.hhp.ecommerce.interfaces.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderRequest {
    private UUID userId;
    private List<OrderItem> items;

    @Data
    public static class OrderItem {
        private UUID productId;
        private int quantity;
    }
}