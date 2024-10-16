package com.hhp.ecommerce.application.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public List<Product> getProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
	}
}
