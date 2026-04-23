package com.expensemargin.authsrv.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String message;
    private String userId;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

}
