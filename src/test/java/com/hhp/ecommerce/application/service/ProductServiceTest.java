package com.hhp.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.ProductRepository;

class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 상품_조회_성공() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 10);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		Product foundProduct = productService.getProductById(productId);

		// then
		assertNotNull(foundProduct);
		assertEquals("테스트 상품", foundProduct.getName());
		assertEquals(1000, foundProduct.getPrice());
		assertEquals(10, foundProduct.getStock());
		verify(productRepository).findById(productId);
	}

	@Test
	void 상품_조회_실패_존재하지_않는_상품() {
		// given
		Long productId = 999L;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		assertThrows(NoSuchElementException.class, () -> productService.getProductById(productId));
		verify(productRepository).findById(productId);
	}

	@Test
	void 재고_감소_성공() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 20);
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.of(product));

		// when
		productService.reduceStock(productId, 5);

		// then
		assertEquals(15, product.getStock());
		verify(productRepository).save(product);
	}

	@Test
	void 재고_감소_실패_재고부족() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 3);
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.of(product));

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			productService.reduceStock(productId, 5));
		assertEquals("재고가 부족합니다.", exception.getMessage());
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	void 재고_복구_성공() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 10);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		productService.rollbackStock(productId, 5);

		// then
		assertEquals(15, product.getStock());
		verify(productRepository).save(product);
	}

	@Test
	void 재고_체크_성공() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 10);
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.of(product));

		// when & then
		assertDoesNotThrow(() -> productService.checkStockAvailability(productId, 5));
		verify(productRepository).findByIdWithLock(productId);
	}

	@Test
	void 재고_체크_실패_재고부족() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 3);
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.of(product));

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
			productService.checkStockAvailability(productId, 5));
		assertEquals("재고가 부족합니다.", exception.getMessage());
	}
}
