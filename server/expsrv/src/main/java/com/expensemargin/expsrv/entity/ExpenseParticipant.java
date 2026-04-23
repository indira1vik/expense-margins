package com.expensemargin.expsrv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "epeople")
@Data
public class ExpenseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long expenseId;
    private String userId;
    private Double amountOwed;
    private Double percentage;
    private String status;
}
