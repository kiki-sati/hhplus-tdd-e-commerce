package com.hhp.ecommerce.presentation.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {
	private UUID cartId;
	private UUID productId;
	private String productName;
	private int quantity;
}