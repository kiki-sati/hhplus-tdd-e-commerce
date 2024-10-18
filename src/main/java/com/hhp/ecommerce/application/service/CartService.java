package com.hhp.ecommerce.application.service;

import com.hhp.ecommerce.domain.model.Cart;
import com.hhp.ecommerce.domain.model.Product;
import com.hhp.ecommerce.infra.persistence.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Transactional
    public void addToCart(Long userId,Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> new Cart(userId));
        Product product = productService.getProductById(productId);
        cart.addItem(product, quantity);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
        public Cart getCart(Long userId) {
            return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));
        }
}
