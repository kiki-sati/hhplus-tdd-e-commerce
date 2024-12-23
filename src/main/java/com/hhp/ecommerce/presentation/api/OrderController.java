package com.hhp.ecommerce.presentation.api;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.application.service.OrderService;
import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderItem;
import com.hhp.ecommerce.presentation.dto.OrderRequest;
import com.hhp.ecommerce.presentation.dto.OrderResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Tag(name = "Order API", description = "주문/결제 관련 API")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
		Order order = Order.create(request.getUserId(),
			request.getItems().stream()
				.map(item -> OrderItem.create(item.getProductId(), item.getQuantity(), item.getPrice()))
				.collect(Collectors.toList())
		);
		orderService.createOrder(order);
		return ResponseEntity.ok().build();
	}
}