package com.hhp.ecommerce.presentation.dto;

import com.hhp.ecommerce.domain.model.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "상품 응답 DTO")
@Getter
@AllArgsConstructor
public class ProductResponse {

	@Schema(description = "상품 ID", example = "1")
	private Long id;

	@Schema(description = "상품 이름", example = "Laptop")
	private String name;

	@Schema(description = "상품 가격", example = "1500")
	private int price;

	@Schema(description = "상품 재고 수량", example = "50")
	private int stock;

	public static ProductResponse fromProduct(Product product) {
		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getPrice(),
			product.getStock()
		);
	}
}
