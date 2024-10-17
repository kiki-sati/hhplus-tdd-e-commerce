package com.hhp.ecommerce.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderItem;
import com.hhp.ecommerce.infra.persistence.OrderRepository;
import com.hhp.ecommerce.presentation.dto.OrderRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductService productService;

	@Transactional
	public void createOrder(OrderRequest request) {
		List<OrderItem> orderItems = request.getItems().stream()
			.map(itemRequest -> {
				productService.checkStockAvailability(itemRequest.getProductId(), itemRequest.getQuantity());
				int price = productService.getProductPrice(itemRequest.getProductId());
				return OrderItem.create(itemRequest.getProductId(), itemRequest.getQuantity(), price);
			})
			.collect(Collectors.toList());

		// 주문 생성
		Order order = Order.create(request.getUserId(), orderItems);
		orderRepository.save(order);
	}

}
