package com.expensemargin.authsrv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileSyncRequest {
    private String userId;
    private String name;
    private String email;
    private String avatarUrl;
    private String status;
}
