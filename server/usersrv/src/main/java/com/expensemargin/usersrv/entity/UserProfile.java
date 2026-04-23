package com.expensemargin.usersrv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "euser_profiles")
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userId;
    private String name;
    private String email;
    private String avatarUrl;
    private String status;
}
