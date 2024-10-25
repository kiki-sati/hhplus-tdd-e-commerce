package com.hhp.ecommerce.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 255)
	private String email;

	@Column(name = "balance", nullable = false)
	private int balance;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private User(String name, String email, int balance) {
		this.name = name;
		this.email = email;
		this.balance = balance;
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
		if (this.balance < amount) {
			throw new IllegalStateException("잔액이 부족합니다.");
		}
		this.balance -= amount;
	}

	public void increaseBalance(int amount) {
		this.balance += amount;
	}
}
