package com.hhp.ecommerce.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hhp.ecommerce.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
