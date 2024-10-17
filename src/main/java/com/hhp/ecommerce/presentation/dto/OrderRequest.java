package com.hhp.ecommerce.presentation.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderRequest {
	@NotNull(message = "사용자 ID는 필수입니다.")
	private Long userId;

	@NotEmpty(message = "주문 항목은 하나 이상이어야 합니다.")
	private List<ItemRequest> items;
}
