package com.hhp.ecommerce.presentation.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.hhp.ecommerce.application.service.ProductService;
import com.hhp.ecommerce.presentation.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.presentation.dto.ProductSalesResponse;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> products = productService.getProducts().stream()
                .map(product -> ProductResponse.fromProduct(product))
                .collect(Collectors.toList());

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
