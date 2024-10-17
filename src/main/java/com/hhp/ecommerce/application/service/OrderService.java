package com.hhp.ecommerce.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderItem;
import com.hhp.ecommerce.infra.persistence.OrderRepository;
import com.hhp.ecommerce.infra.persistence.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductService productService;
	private final UserRepository userRepository;

	@Transactional
	public Order createOrder(Order order) {
		// 주문 항목들에 대한 확인 및 생성
		List<OrderItem> orderItems = order.getOrderItems().stream()
			.map(itemRequest -> {
				// 상품 재고 확인
				productService.checkStockAvailability(itemRequest.getProductId(), itemRequest.getQuantity());
				// 상품 가격 가져오기
				int price = productService.getProductPrice(itemRequest.getProductId());
				// OrderItem 생성
				return OrderItem.create(itemRequest.getProductId(), itemRequest.getQuantity(), price);
			})
			.toList();

		// 주문 객체 생성 및 항목 추가
		Order newOrder = Order.create(order.getUserId(), orderItems);

		// Order 저장
		Order savedOrder = orderRepository.save(newOrder);
		// 만약 데이터베이스에 저장된 후 ID가 설정되었는지 확인
		if (savedOrder.getId() == null) {
			throw new IllegalStateException("주문이 데이터베이스에 저장되지 않았습니다.");
		}

		return savedOrder;
	}

}
