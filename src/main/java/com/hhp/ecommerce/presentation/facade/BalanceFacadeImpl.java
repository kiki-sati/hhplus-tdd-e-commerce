package com.hhp.ecommerce.presentation.facade;

import org.springframework.stereotype.Service;

import com.hhp.ecommerce.application.service.UserService;
import com.hhp.ecommerce.domain.model.BalanceHistory;
import com.hhp.ecommerce.infra.persistence.BalanceHistoryRepository;
import com.hhp.ecommerce.presentation.dto.BalanceRequest;
import com.hhp.ecommerce.presentation.dto.BalanceResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BalanceFacadeImpl implements BalanceFacade {

	private final UserService userService;
	private final BalanceHistoryRepository balanceHistoryRepository;

	@Override
	public BalanceResponse chargeBalance(BalanceRequest balanceRequest) {
		var user = userService.findUserById(balanceRequest.getUserId());

		balanceHistoryRepository.save(BalanceHistory.create(user.getId(), balanceRequest.getAmount(), "CHARGE"));

		user.increaseBalance(balanceRequest.getAmount());
		userService.updateUser(user);

		return new BalanceResponse(user.getId(), user.getBalance());
	}

	@Override
	public BalanceResponse getBalance(Long userId) {
		var user = userService.findUserById(userId);
		return new BalanceResponse(user.getId(), user.getBalance());
	}
}
