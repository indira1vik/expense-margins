package com.expensemargin.expsrv.repository;

import com.expensemargin.expsrv.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
