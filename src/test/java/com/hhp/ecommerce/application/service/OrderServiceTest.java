package com.hhp.ecommerce.application.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hhp.ecommerce.infra.persistence.OrderRepository;

class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

}