package com.hhp.ecommerce.interfaces.api;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.interfaces.dto.BalanceRequest;
import com.hhp.ecommerce.interfaces.dto.BalanceResponse;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private static final Map<UUID, Integer> MOCK_BALANCE_DB = new HashMap<>();

    @PostMapping("/charge")
    public ResponseEntity<BalanceResponse> chargeBalance(@RequestBody BalanceRequest request) {
        UUID userId = request.getUserId();
        int newBalance = MOCK_BALANCE_DB.getOrDefault(userId, 0) + request.getAmount();
        MOCK_BALANCE_DB.put(userId, newBalance);

        BalanceResponse response = new BalanceResponse(userId, newBalance);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BalanceResponse> getBalance(@RequestParam UUID userId) {
        int balance = MOCK_BALANCE_DB.getOrDefault(userId, 0);
        BalanceResponse response = new BalanceResponse(userId, balance);
        return ResponseEntity.ok(response);
    }
}