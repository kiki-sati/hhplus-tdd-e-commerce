package com.hhp.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.hhp.ecommerce.domain.model.User;
import com.hhp.ecommerce.infra.persistence.UserRepository;

@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@Test
	void 잔액_차감_실패_잔액부족() {
		// given
		Long userId = 1L;
		int amount = 5000;

		User user = User.create("테스트 사용자" , "test@naver.com", 3000);
		Mockito.when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(user));

		// when & then
		assertThrows(IllegalStateException.class, () -> userService.deductBalance(userId, amount));
	}

	@Test
	void 잔액_차감_성공() {
		// given
		Long userId = 1L;
		int amount = 1000;

		User user = User.create("테스트 사용자" , "test@naver.com", 3000);
		Mockito.when(userRepository.findByIdWithLock(userId)).thenReturn(Optional.of(user));

		// when
		User result = userService.deductBalance(userId, amount);

		// then
		assertEquals(2000, user.getBalance());
	}
}
