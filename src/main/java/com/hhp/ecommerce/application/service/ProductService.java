package com.hhp.ecommerce.application.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public int getProductPrice(Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
		return product.getPrice();
	}

	@Transactional
	public void reduceStock(Long productId, int quantity) {
		Optional<Product> productOptional = productRepository.findByIdWithLock(productId);
		Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

		product.reduceStock(quantity);
		productRepository.save(product);
	}

	public void rollbackStock(Long productId, int quantity) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

		product.increaseStock(quantity);
		productRepository.save(product);
	}

	public void checkStockAvailability(Long productId, int quantity) {
		Product product = productRepository.findByIdWithLock(productId)
			.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
		if (product.getStock() < quantity) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		}
	}
}
