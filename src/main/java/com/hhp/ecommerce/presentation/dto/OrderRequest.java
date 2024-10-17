package com.hhp.ecommerce.presentation.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
	@NotNull(message = "사용자 ID는 필수입니다.")
	private Long userId;

	@NotEmpty(message = "주문 항목은 하나 이상이어야 합니다.")
	private List<OrderItem> items;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItem {

		@NotNull(message = "상품 ID는 필수입니다.")
		private Long productId;

		@Positive(message = "수량은 0보다 커야 합니다.")
		private int quantity;

		@NotNull(message = "가격은 필수입니다.")
		private int price;
	}

}