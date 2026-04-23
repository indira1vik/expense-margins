package com.expensemargin.expsrv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SettlementResponse {
    private Long id;
    private Long expenseId;
    private String fromUserId;
    private String toUserId;
    private Double amount;
    private String status;
}
