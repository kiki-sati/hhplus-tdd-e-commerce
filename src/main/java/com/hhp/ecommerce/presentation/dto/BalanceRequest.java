package com.hhp.ecommerce.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "잔액 충전 요청 DTO")
public class BalanceRequest {

    @Schema(description = "사용자 ID", example = "1L")
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @Schema(description = "금액", example = "10000")
    @NotNull(message = "금액은 필수 입니다.")
    private int amount;
}

