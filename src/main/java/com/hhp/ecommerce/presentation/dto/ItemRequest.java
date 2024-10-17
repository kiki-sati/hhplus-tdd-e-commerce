package com.hhp.ecommerce.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ItemRequest {

	@NotNull(message = "상품 ID는 필수입니다.")
	private Long productId;

	@Positive(message = "수량은 0보다 커야 합니다.")
	private int quantity;
}
