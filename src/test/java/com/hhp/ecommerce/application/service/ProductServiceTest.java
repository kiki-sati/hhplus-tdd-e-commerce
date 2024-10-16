package com.hhp.ecommerce.application.service;

import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.ProductRepository;
import com.hhp.ecommerce.presentation.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    public void 성공_상품목록() {
        Product product1 = Product.create("Product1", 100, 10);
        Product product2 = Product.create("Product2", 200, 5);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getProducts();

        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
        assertEquals("Product2", products.get(1).getName());
    }

}