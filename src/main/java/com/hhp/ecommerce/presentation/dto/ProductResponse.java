package com.hhp.ecommerce.presentation.dto;

import com.hhp.ecommerce.domain.model.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
	private Long id;
	private String name;
	private int price;
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
