package com.hhp.ecommerce.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hhp.ecommerce.domain.model.BalanceHistory;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
}
