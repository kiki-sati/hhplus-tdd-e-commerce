package com.hhp.ecommerce.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
	private String orderId;    // 주문 ID
	private String userId;     // 사용자 ID
	private int totalPrice;    // 총 결제 금액
	private String orderStatus;  // 주문 상태
}