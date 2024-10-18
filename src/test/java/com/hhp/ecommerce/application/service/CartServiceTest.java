package com.hhp.ecommerce.application.service;

import com.hhp.ecommerce.domain.model.Cart;
import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private CartRepository cartRepository;
    private ProductService productService;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartRepository = Mockito.mock(CartRepository.class);
        productService = Mockito.mock(ProductService.class);
        cartService = new CartService(cartRepository, productService);
    }

    @Test
    void 장바구니에_상품_추가() {
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 3;

        Product product = Product.create("Test Product", 1000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        cartService.addToCart(userId, productId, quantity);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void 장바구니에서_상품_삭제() {
        Long userId = 1L;
        Long productId = 1L;

        Cart cart = new Cart(userId);
        Product product = Product.create("Test Product", 1000, 10);
        cart.addItem(product, 5);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.removeFromCart(userId, productId);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void 장바구니_조회() {
        Long userId = 1L;

        Cart cart = new Cart(userId);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }
}
