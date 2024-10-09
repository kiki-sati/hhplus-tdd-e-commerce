package com.hhp.ecommerce.interfaces.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 잔액 응답 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {
	private UUID userId;
	private int balance;
}
