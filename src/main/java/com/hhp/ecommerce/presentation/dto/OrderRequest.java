package com.hhp.ecommerce.presentation.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 요청 DTO")
public class OrderRequest {
    @Schema(description = "사용자 ID", example = "1L")
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotEmpty(message = "주문 항목은 하나 이상이어야 합니다.")
    private List<OrderItem> items;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        @Schema(description = "상품 ID", example = "1L")
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long productId;

        @Schema(description = "수랑", example = "1")
        @Positive(message = "수량은 0보다 커야 합니다.")
        private int quantity;

        @Schema(description = "가격", example = "10000")
        @NotNull(message = "가격은 필수 입니다.")
        private int price;
    }

}