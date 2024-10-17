package com.hhp.ecommerce.domain.model;

public enum OrderStatus {
	PENDING,
	SUCCESS,
	FAILED,
	CANCELED;

	public static OrderStatus fromString(String status) {
		try {
			return OrderStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("잘못된 상태 값입니다: " + status);
		}
	}
}