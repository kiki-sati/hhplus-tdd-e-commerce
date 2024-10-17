package com.hhp.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhp.ecommerce.domain.model.Payment;
import com.hhp.ecommerce.domain.model.User;
import com.hhp.ecommerce.infra.persistence.PaymentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final UserService userService;

	@Transactional
	public Payment processPayment(Long orderId, String idempotencyKey, int amount, Long userId) {
		paymentRepository.findByIdempotencyKey(idempotencyKey)
			.ifPresent(payment -> {
				throw new IllegalStateException("이미 처리된 요청입니다.");
			});

		Payment payment = Payment.create(orderId, idempotencyKey, userId);
		paymentRepository.save(payment);

		User user = userService.getUserWithPessimisticLock(payment.getUserId());
		user.deductBalance(amount);
		payment.completePayment();

		paymentRepository.save(payment);
		return payment;
	}

	@Transactional
	public void completePayment(Long paymentId) {
		Payment payment = paymentRepository.findByIdWithSharedLock(paymentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 결제를 찾을 수 없습니다."));

		payment.completePayment();
		paymentRepository.save(payment);
	}

	@Transactional
	public void failPayment(Payment payment) {
		payment.failPayment();
		paymentRepository.save(payment);
	}

}
