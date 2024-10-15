package com.hhp.ecommerce.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.presentation.dto.BalanceRequest;
import com.hhp.ecommerce.presentation.dto.BalanceResponse;

@RestController
@RequestMapping("/balance")
public class BalanceController {

	// 잔액 충전 API
	@PostMapping("/charge")
	public ResponseEntity<BalanceResponse> chargeBalance(@RequestBody BalanceRequest request) {
		BalanceResponse response = new BalanceResponse(request.getUserId(), request.getAmount() + 1000);
		return ResponseEntity.ok(response);
	}

	// 잔액 조회 API
	@GetMapping
	public ResponseEntity<BalanceResponse> getBalance(@RequestParam String userId) {
		BalanceResponse response = new BalanceResponse(userId, 1000);
		return ResponseEntity.ok(response);
	}
}
