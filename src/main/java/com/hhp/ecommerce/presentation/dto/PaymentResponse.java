package com.hhp.ecommerce.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "결제 응답 DTO")
public class PaymentResponse {

    @Schema(description = "결제 성공 여부", example = "true")
    private boolean paymentSuccessful;

    @Schema(description = "결제 메시지", example = "결제 및 주문 완료")
    private String message;
}