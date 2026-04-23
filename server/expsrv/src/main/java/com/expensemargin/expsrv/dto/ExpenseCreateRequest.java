package com.expensemargin.expsrv.dto;

import lombok.Data;

@Data
public class ExpenseCreateRequest {
    private String title;
    private Double totalAmount;
    private String currency;
    private String createdByUserId;
    private String splitType;
    private java.util.List<ExpenseUserSplit> splits;
}
