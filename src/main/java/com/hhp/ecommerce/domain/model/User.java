package com.hhp.ecommerce.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 255)
	private String email;

	@Column(name = "current_balance", nullable = false)
	private int currentBalance;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private User(String name, String email, int currentBalance) {
		this.name = name;
		this.email = email;
		this.currentBalance = currentBalance;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public static User create(String name, String email, int currentBalance) {
		return new User(name, email, currentBalance);
	}

	public void deductBalance(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("차감할 금액은 0보다 커야 합니다.");
		}
		if (this.currentBalance < amount) {
			throw new IllegalStateException("잔액이 부족합니다.");
		}
		this.currentBalance -= amount;
	}
}
