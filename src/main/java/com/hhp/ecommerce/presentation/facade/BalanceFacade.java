package com.hhp.ecommerce.presentation.facade;

import com.hhp.ecommerce.presentation.dto.BalanceRequest;
import com.hhp.ecommerce.presentation.dto.BalanceResponse;

public interface BalanceFacade {

    BalanceResponse chargeBalance(BalanceRequest balanceRequest);

    BalanceResponse getBalance(Long userId);
}
