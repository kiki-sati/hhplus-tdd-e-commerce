package com.hhp.ecommerce.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
	private String productId;  // 상품 ID
	private int quantity;      // 주문할 수량
}