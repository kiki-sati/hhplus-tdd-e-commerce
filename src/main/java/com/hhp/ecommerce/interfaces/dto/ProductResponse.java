package com.hhp.ecommerce.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	private String productId;
	private String name;
	private int price;
	private int stock;
}
