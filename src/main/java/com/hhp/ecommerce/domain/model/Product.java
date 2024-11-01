package com.hhp.ecommerce.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int stock;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private Product(String name, int price, int stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public static Product create(String name, int price, int stock) {
		return new Product(name, price, stock);
	}

	public synchronized void reduceStock(int quantity) {
	    if (this.stock < quantity) {
	        throw new IllegalArgumentException("재고가 부족합니다.");
	    }
	    this.stock -= quantity;
	    this.updatedAt = LocalDateTime.now();
	}

	public void increaseStock(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
		}
		this.stock += quantity;
		this.updatedAt = LocalDateTime.now();
	}
}
