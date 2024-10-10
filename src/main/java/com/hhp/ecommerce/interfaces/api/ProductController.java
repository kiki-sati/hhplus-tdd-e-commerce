package com.hhp.ecommerce.interfaces.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.interfaces.dto.ProductResponse;
import com.hhp.ecommerce.interfaces.dto.ProductSalesResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts() {
		List<ProductResponse> products = new ArrayList<>();
		products.add(new ProductResponse(UUID.randomUUID().toString(), "Laptop", 1500, 10));
		products.add(new ProductResponse(UUID.randomUUID().toString(), "Smartphone", 800, 20));

		return ResponseEntity.ok(products);
	}

	@GetMapping("/top")
	public ResponseEntity<List<ProductSalesResponse>> getTopProducts() {
		List<ProductSalesResponse> topProducts = new ArrayList<>();
		topProducts.add(new ProductSalesResponse(UUID.randomUUID().toString(), "Laptop", 1500, 25));
		topProducts.add(new ProductSalesResponse(UUID.randomUUID().toString(), "Smartphone", 800, 20));

		return ResponseEntity.ok(topProducts);
	}
}
