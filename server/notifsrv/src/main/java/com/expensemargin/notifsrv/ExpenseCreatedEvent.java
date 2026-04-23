package com.expensemargin.notifsrv;

import lombok.Data;

@Data
public class ExpenseCreatedEvent {
    private Long expenseId;
    private String title;
    private Double totalAmount;
    private String createdByUserId;
    private String splitType;
}