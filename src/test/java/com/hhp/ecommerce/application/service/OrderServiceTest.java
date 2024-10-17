package com.hhp.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hhp.ecommerce.domain.model.Order;
import com.hhp.ecommerce.domain.model.OrderItem;
import com.hhp.ecommerce.infra.persistence.OrderRepository;

class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private ProductService productService;

	@Mock
	private OrderRepository orderRepository;

	public OrderServiceTest() {
		MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
	}

	@Test
	void 주문_생성_실패_잘못된_상품_ID() {
		// given
		Long userId = 1L;
		Long invalidProductId = -1L;

		when(productService.getProductPrice(invalidProductId))
			.thenThrow(new IllegalArgumentException("잘못된 상품 ID입니다."));

		List<OrderItem> items = List.of(OrderItem.create(invalidProductId, 1, 1000));

		// when & then
		assertThrows(IllegalArgumentException.class, () -> {
			Order order = Order.create(userId, items);
			orderService.createOrder(order);
		});

		verify(productService).getProductPrice(invalidProductId);
	}

	@Test
	void 주문_생성_성공() {
		// given
		Long userId = 1L;
		Long productId = 1L;
		int price = 1000;

		when(productService.getProductPrice(productId)).thenReturn(price);

		List<OrderItem> items = List.of(OrderItem.create(productId, 1, price));
		Order order = Order.create(userId, items);

		when(orderRepository.save(order)).thenReturn(order);

		// when
		Order savedOrder = orderService.createOrder(order);

		// then
		assertNotNull(savedOrder);  // 이 부분에서 실패하는 경우가 있음
		assertEquals(userId, savedOrder.getUserId());
		assertEquals(1, savedOrder.getOrderItems().size());
		assertEquals(price, savedOrder.getTotalPrice());

		verify(productService).getProductPrice(productId);
		verify(orderRepository).save(order);
	}
}
