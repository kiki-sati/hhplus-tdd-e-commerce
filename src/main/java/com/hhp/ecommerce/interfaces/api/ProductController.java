package com.hhp.ecommerce.interfaces.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.interfaces.dto.ProductResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

	@GetMapping
	public List<ProductResponse> getProducts() {

		List<ProductResponse> products = new ArrayList<>();
		products.add(new ProductResponse(UUID.randomUUID(), "Product A", 1000, 50));
		products.add(new ProductResponse(UUID.randomUUID(), "Product B", 2000, 20));
		products.add(new ProductResponse(UUID.randomUUID(), "Product C", 3000, 30));
		return products;
	}
}