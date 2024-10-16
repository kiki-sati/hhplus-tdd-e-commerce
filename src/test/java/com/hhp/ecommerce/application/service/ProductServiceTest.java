package com.hhp.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.ProductRepository;

class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 성공_상품목록() {
		Product product1 = Product.create("Product1", 100, 10);
		Product product2 = Product.create("Product2", 200, 5);

		when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

		List<Product> products = productService.getProducts();

		assertEquals(2, products.size());
		assertEquals("Product1", products.get(0).getName());
		assertEquals("Product2", products.get(1).getName());
	}

	@Test
	void 성공_특정_상품_조회() {
		// given
		Long productId = 1L;
		Product product = Product.create("테스트 상품", 1000, 50);
		ReflectionTestUtils.setField(product, "id", productId);
		Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		Product foundProduct = productService.getProductById(productId);

		// then
		assertEquals(productId, foundProduct.getId());
		assertEquals("테스트 상품", foundProduct.getName());
	}

	@Test
	void 실패_특정_상품_조회() {
		// given
		Long productId = 99L;
		Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		assertThrows(NoSuchElementException.class, () -> productService.getProductById(productId));
	}
}