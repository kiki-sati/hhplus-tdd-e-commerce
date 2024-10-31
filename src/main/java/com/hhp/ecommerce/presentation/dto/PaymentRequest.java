package com.hhp.ecommerce.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "결제 요청 DTO")
public class PaymentRequest {

    @Schema(description = "주문 ID", example = "1001")
    @NotNull(message = "주문 ID는 필수입니다.")
    private Long orderId;

    @Schema(description = "사용자 ID", example = "1")
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @Schema(description = "결제 금액", example = "15000")
    @NotNull(message = "결제 금액은 필수입니다.")
    private int amount;

    @Schema(description = "멱등성 키", example = "unique_key_1234")
    private String idempotencyKey;
}