package com.hhp.ecommerce.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long orderId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Column(nullable = false)
	private String idempotencyKey;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private Payment(Long orderId, String idempotencyKey, Long userId) {
		this.orderId = orderId;
		this.userId = userId;
		this.idempotencyKey = idempotencyKey;
		this.status = PaymentStatus.PENDING;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public static Payment create(Long orderId, String idempotencyKey, Long userId) {
		return new Payment(orderId, idempotencyKey, userId);
	}

	public void completePayment() {
		if (this.status != PaymentStatus.PENDING) {
			throw new IllegalStateException("결제 상태가 PENDING이 아니므로 완료할 수 없습니다.");
		}
		this.status = PaymentStatus.SUCCESS;
	}

	public void failPayment() {
		if (this.status != PaymentStatus.PENDING) {
			throw new IllegalStateException("결제 상태가 PENDING이 아니므로 실패 처리할 수 없습니다.");
		}
		this.status = PaymentStatus.FAILED;
	}

}