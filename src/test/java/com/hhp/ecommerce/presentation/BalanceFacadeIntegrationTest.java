package com.hhp.ecommerce.presentation;

import com.hhp.ecommerce.application.service.UserService;
import com.hhp.ecommerce.domain.model.BalanceHistory;
import com.hhp.ecommerce.domain.model.User;
import com.hhp.ecommerce.infra.persistence.BalanceHistoryRepository;
import com.hhp.ecommerce.infra.persistence.UserRepository;
import com.hhp.ecommerce.presentation.dto.BalanceRequest;
import com.hhp.ecommerce.presentation.dto.BalanceResponse;
import com.hhp.ecommerce.presentation.facade.BalanceFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BalanceFacadeIntegrationTest {

    @Autowired
    private BalanceFacade balanceFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceHistoryRepository balanceHistoryRepository;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = User.create("Test User", "test@naver.com",100);
        userRepository.save(testUser);
    }

    @Test
    public void testChargeBalance() {
        // Given: 충전 요청 생성
        BalanceRequest balanceRequest = new BalanceRequest();
        balanceRequest.setUserId(testUser.getId());
        balanceRequest.setAmount(50);

        // When: 잔액 충전 요청
        BalanceResponse response = balanceFacade.chargeBalance(balanceRequest);

        // Then: 잔액이 충전되었는지 확인
        User updatedUser = userService.findUserById(testUser.getId());
        assertThat(updatedUser.getBalance()).isEqualTo(150);
        assertThat(response.getBalance()).isEqualTo(150);

        // 잔액 충전 기록이 남았는지 확인
        BalanceHistory history = balanceHistoryRepository.findAll().get(0);
        assertThat(history.getUserId()).isEqualTo(testUser.getId());
        assertThat(history.getAmount()).isEqualTo(50);
        assertThat(history.getTransactionType()).isEqualTo("CHARGE");
    }

    @Test
    public void testGetBalance() {
        // Given: 기존 사용자 잔액 100 설정됨

        // When: 잔액 조회 요청
        BalanceResponse response = balanceFacade.getBalance(testUser.getId());

        // Then: 잔액이 올바르게 반환되었는지 확인
        assertThat(response.getBalance()).isEqualTo(100);
    }
}
