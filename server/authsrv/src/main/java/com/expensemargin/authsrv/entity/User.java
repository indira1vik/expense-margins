package com.expensemargin.authsrv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "eusers")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    private String name;
    private String userid;
}
