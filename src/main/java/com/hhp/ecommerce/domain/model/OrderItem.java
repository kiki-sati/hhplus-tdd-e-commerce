package com.hhp.ecommerce.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItem {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	private Long productId;
	private int quantity;
	private int price;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public void setId(Long id) {
		this.id = id;
	}

	private OrderItem(Long productId, int quantity, int price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

	}

	public static OrderItem create(Long productId, int quantity, int price) {
		return new OrderItem(productId, quantity, price);
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
