package com.hhp.ecommerce.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhp.ecommerce.domain.model.User;
import com.hhp.ecommerce.infra.persistence.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public void deductBalance(Long userId, int amount) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		user.deductBalance(amount);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public int getUserBalance(Long userId) {
		Optional<User> userOptional = userRepository.findByIdWithSharedLock(userId);
		User user = userOptional.orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
		
		return user.getCurrentBalance();
	}
}
