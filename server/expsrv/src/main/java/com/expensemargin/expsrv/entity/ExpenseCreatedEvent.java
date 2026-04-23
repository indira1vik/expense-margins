package com.expensemargin.expsrv.entity;

import lombok.Data;

@Data
public class ExpenseCreatedEvent {
    private Long expenseId;
    private String title;
    private Double totalAmount;
    private String createdByUserId;
    private String splitType;

    public ExpenseCreatedEvent(
            Long expenseId,
            String title,
            Double totalAmount,
            String createdByUserId,
            String splitType
    ) {
       this.expenseId = expenseId;
       this.title = title;
       this.totalAmount = totalAmount;
       this.createdByUserId = createdByUserId;
       this.splitType = splitType;
    }
}
