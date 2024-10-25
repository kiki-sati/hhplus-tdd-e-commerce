package com.hhp.ecommerce.presentation.dto;

import com.hhp.ecommerce.domain.model.Cart;
import com.hhp.ecommerce.domain.model.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Schema(description = "장바구니 요청 DTO")
public class CartResponse {
    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "장바구니 항목 목록")
    private List<CartItemResponse> items;

    public static CartResponse fromCart(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(CartItemResponse::fromCartItem)
                .collect(Collectors.toList());
        return new CartResponse(cart.getUserId(), itemResponses);
    }

    @Getter
    @AllArgsConstructor
    public static class CartItemResponse {
        @Schema(description = "상품 ID")
        private Long productId;

        @Schema(description = "상품 이름")
        private String productName;

        @Schema(description = "수량")
        private int quantity;

        public static CartItemResponse fromCartItem(CartItem cartItem) {
            return new CartItemResponse(
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity()
            );
        }
    }
}
