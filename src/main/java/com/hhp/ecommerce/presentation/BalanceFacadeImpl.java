package com.hhp.ecommerce.presentation;

import com.hhp.ecommerce.application.service.UserService;
import com.hhp.ecommerce.domain.model.BalanceHistory;
import com.hhp.ecommerce.presentation.dto.BalanceRequest;
import com.hhp.ecommerce.presentation.dto.BalanceResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BalanceFacadeImpl implements BalanceFacade {

    private final UserService userService;

    @Override
    public BalanceResponse chargeBalance(BalanceRequest balanceRequest) {
        var user = userService.findUserById(balanceRequest.getUserId());
        BalanceHistory balanceHistory = BalanceHistory.create(user, balanceRequest.getAmount(), "CHARGE");
        user.getBalanceHistory().add(balanceHistory);

        // 잔액 업데이트
        user.increaseBalance(balanceRequest.getAmount());
        userService.updateUser(user);

        return new BalanceResponse(user.getId(), user.getBalance());

        return null;
    }

    @Override
    public BalanceResponse getBalance(Long userId) {
        return null;
    }
}
