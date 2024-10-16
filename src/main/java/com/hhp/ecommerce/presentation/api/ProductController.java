package com.hhp.ecommerce.presentation.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.application.service.ProductService;
import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.presentation.dto.ProductSalesResponse;
import com.hhp.ecommerce.presentation.dto.response.ProductResponse;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts() {
		List<ProductResponse> products = productService.getProducts().stream()
			.map(ProductResponse::fromProduct)
			.toList();

		return ResponseEntity.ok(products);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		ProductResponse productResponse = ProductResponse.fromProduct(product);

		return ResponseEntity.ok(productResponse);
	}

	@GetMapping("/top")
	public ResponseEntity<List<ProductSalesResponse>> getTopProducts() {
		List<ProductSalesResponse> topProducts = new ArrayList<>();
		topProducts.add(new ProductSalesResponse(UUID.randomUUID().toString(), "Laptop", 1500, 25));
		topProducts.add(new ProductSalesResponse(UUID.randomUUID().toString(), "Smartphone", 800, 20));

		return ResponseEntity.ok(topProducts);
	}
}
