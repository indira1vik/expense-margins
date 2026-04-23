package com.expensemargin.expsrv.dto;

import com.expensemargin.expsrv.entity.Expense;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExpenseCreateResponse {
    private Expense expense;
    private List<SettlementResponse> settlements;
}
