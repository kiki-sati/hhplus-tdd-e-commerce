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
import com.hhp.ecommerce.presentation.dto.ProductResponse;
import com.hhp.ecommerce.presentation.dto.ProductSalesResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Product Management", description = "상품 관리 API")  // 기존 @Api 대체
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@Operation(summary = "모든 상품 조회", description = "현재 등록된 모든 상품의 목록을 조회합니다.")  // 기존 @ApiOperation 대체
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts() {
		List<ProductResponse> products = productService.getProducts().stream()
			.map(ProductResponse::fromProduct)
			.toList();

		return ResponseEntity.ok(products);
	}

	@Operation(summary = "상품 조회", description = "ID를 기반으로 특정 상품을 조회합니다.")  // 기존 @ApiOperation 대체
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		ProductResponse productResponse = ProductResponse.fromProduct(product);

		return ResponseEntity.ok(productResponse);
	}

	@Operation(summary = "인기 상품 조회", description = "판매량 기준 상위 인기 상품을 조회합니다.")  // 기존 @ApiOperation 대체
	@GetMapping("/top")
	public ResponseEntity<List<ProductSalesResponse>> getTopProducts() {
		List<ProductSalesResponse> topProducts = new ArrayList<>();
		topProducts.add(new ProductSalesResponse(UUID.randomUUID().toString(), "Laptop", 1500, 25));
		topProducts.add(new ProductSalesResponse(UUID.randomUUID().toString(), "Smartphone", 800, 20));

		return ResponseEntity.ok(topProducts);
	}
}
