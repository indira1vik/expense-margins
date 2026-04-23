package com.expensemargin.expsrv.dto;

import lombok.Data;

@Data
public class ExpenseUserSplit {
    private String userId;
    private Double amount;
    private Double percentage;
}
