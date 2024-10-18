package com.hhp.ecommerce.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "주문 응답 DTO")
public class OrderResponse {
    @Schema(description = "주문 ID", example = "1001")
    private Long orderId;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "총 가격", example = "15000")
    private int totalPrice;

    @Schema(description = "주문 상태", example = "PENDING")
    private String orderStatus;
}
