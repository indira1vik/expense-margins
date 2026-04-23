package com.expensemargin.expsrv.repository;

import com.expensemargin.expsrv.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
