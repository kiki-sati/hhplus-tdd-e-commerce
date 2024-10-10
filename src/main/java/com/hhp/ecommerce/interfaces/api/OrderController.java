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
		// Mock 데이터로 주문 정보 생성
		int totalPrice = request.getItems().stream()
			.mapToInt(item -> item.getQuantity() * 1000)  // Mock 가격 1000원/상품 적용
			.sum();

		// 주문 결과를 응답
		OrderResponse response = new OrderResponse(
			UUID.randomUUID().toString(),
			request.getUserId(),
			totalPrice,
			"SUCCESS"
		);
		return ResponseEntity.ok(response);
	}
}