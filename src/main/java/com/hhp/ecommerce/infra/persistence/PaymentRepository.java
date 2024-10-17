package com.hhp.ecommerce.infra.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.hhp.ecommerce.domain.model.Payment;

import jakarta.persistence.LockModeType;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Payment p WHERE p.id = :paymentId")
	Optional<Payment> findByIdWithSharedLock(Long paymentId);

	Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
