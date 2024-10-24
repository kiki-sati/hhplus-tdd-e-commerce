package com.hhp.ecommerce.presentation.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.presentation.BalanceFacade;
import com.hhp.ecommerce.presentation.dto.BalanceRequest;
import com.hhp.ecommerce.presentation.dto.BalanceResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/balance")
@AllArgsConstructor
@Tag(name = "Balance API", description = "잔액 관련 API")
public class BalanceController {

	private final BalanceFacade balanceFacade;

	@PostMapping("/charge")
	public ResponseEntity<BalanceResponse> chargeBalance(@RequestBody BalanceRequest balanceRequest) {
		BalanceResponse response = balanceFacade.chargeBalance(balanceRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<BalanceResponse> getBalance(@RequestParam Long userId) {
		BalanceResponse response = balanceFacade.getBalance(userId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
