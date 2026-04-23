package com.expensemargin.expsrv.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "expenses")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdByUserId;
    private String title;
    private Double totalAmount;
    private String currency;
    private String type;
    private Date createdAt;
}
