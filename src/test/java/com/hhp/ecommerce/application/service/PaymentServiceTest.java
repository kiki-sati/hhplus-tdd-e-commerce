package com.hhp.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hhp.ecommerce.domain.model.Payment;
import com.hhp.ecommerce.domain.model.PaymentStatus;
import com.hhp.ecommerce.domain.model.User;
import com.hhp.ecommerce.infra.persistence.PaymentRepository;

class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private UserService userService;

	@InjectMocks
	private PaymentService paymentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 결제_성공_테스트() {
		// given
		Long orderId = 1L;
		String idempotencyKey = "unique-key";
		int amount = 1000;
		Long userId = 1L;

		when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());
		User mockUser = Mockito.mock(User.class);
		when(userService.getUserWithPessimisticLock(userId)).thenReturn(mockUser);

		// when
		Payment payment = paymentService.processPayment(orderId, idempotencyKey, amount, userId);

		// then
		assertNotNull(payment);
		assertEquals(orderId, payment.getOrderId());
		assertEquals(idempotencyKey, payment.getIdempotencyKey());
		verify(mockUser).deductBalance(amount);
		verify(paymentRepository, times(2)).save(any(Payment.class)); // 처음 생성할 때와 완료 처리할 때 두 번 저장됨
	}

	@Test
	void 결제_실패_이미_처리된_요청() {
		// given
		Long orderId = 1L;
		String idempotencyKey = "duplicate-key";
		int amount = 1000;
		Long userId = 1L;

		Payment existingPayment = Payment.create(orderId, idempotencyKey, userId);
		when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(existingPayment));

		// when & then
		IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
			paymentService.processPayment(orderId, idempotencyKey, amount, userId));
		assertEquals("이미 처리된 요청입니다.", exception.getMessage());
	}

	@Test
	void 결제_실패_잔액_부족() {
		// given
		Long orderId = 1L;
		String idempotencyKey = "unique-key";
		int amount = 1000;
		Long userId = 1L;

		when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());
		User mockUser = Mockito.mock(User.class);
		when(userService.getUserWithPessimisticLock(userId)).thenReturn(mockUser);
		doThrow(new IllegalStateException("잔액이 부족합니다.")).when(mockUser).deductBalance(amount);

		// when & then
		IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
			paymentService.processPayment(orderId, idempotencyKey, amount, userId));
		assertEquals("잔액이 부족합니다.", exception.getMessage());
		verify(paymentRepository, never()).save(any(Payment.class)); // 결제 실패 시 저장되지 않음
	}

	@Test
	void 결제_완료_성공() {
		// given
		Long paymentId = 1L;
		Payment payment = Payment.create(1L, "unique-key", 1L);
		when(paymentRepository.findByIdWithSharedLock(paymentId)).thenReturn(Optional.of(payment));

		// when
		paymentService.completePayment(paymentId);

		// then
		assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
		verify(paymentRepository).save(payment);
	}

	@Test
	void 결제_완료_실패_존재하지_않는_결제() {
		// given
		Long paymentId = 999L;
		when(paymentRepository.findByIdWithSharedLock(paymentId)).thenReturn(Optional.empty());

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			paymentService.completePayment(paymentId));
		assertEquals("해당 결제를 찾을 수 없습니다.", exception.getMessage());
	}

	@Test
	void 결제_실패_처리_테스트() {
		// given
		Payment payment = Payment.create(1L, "unique-key", 1L);

		// when
		paymentService.failPayment(payment);

		// then
		assertEquals(PaymentStatus.FAILED, payment.getStatus());
		verify(paymentRepository).save(payment);
	}
}
