package com.hhp.ecommerce.infra.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.hhp.ecommerce.domain.model.User;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, Long> {

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT u FROM User u WHERE u.id = :userId")
	Optional<User> findByIdWithSharedLock(Long userId);
}
