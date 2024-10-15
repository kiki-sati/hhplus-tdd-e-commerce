package com.hhp.ecommerce.presentation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	private String userId;
	private List<OrderItemDto> items;  // 주문할 상품 목록
}