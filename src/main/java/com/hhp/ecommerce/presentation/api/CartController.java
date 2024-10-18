package com.hhp.ecommerce.presentation.api;

import com.hhp.ecommerce.application.service.CartService;
import com.hhp.ecommerce.domain.model.Cart;
import com.hhp.ecommerce.presentation.dto.CartResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@Tag(name = "Cart API", description = "장바구니 관련 API")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 추가", description = "사용자의 장바구니에 특정 상품을 추가합니다.")
    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니에서 상품 제거", description = "사용자의 장바구니에서 특정 상품을 제거합니다.")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 조회", description = "사용자의 장바구니를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCart(userId);
        CartResponse cartResponse = CartResponse.fromCart(cart);
        return ResponseEntity.ok(cartResponse);
    }
}
