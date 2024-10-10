package com.hhp.ecommerce.interfaces.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private UUID productId;
    private String name;
    private int price;
    private int stock;
}