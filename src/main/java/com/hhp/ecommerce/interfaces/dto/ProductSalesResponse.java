package com.hhp.ecommerce.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesResponse {
	private String productId;
	private String name;
	private int price;
	private int totalSales;
}
