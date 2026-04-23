package com.expensemargin.expsrv.repository;

import com.expensemargin.expsrv.entity.ExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseParticipantRepo extends JpaRepository<ExpenseParticipant, Long> {
}
