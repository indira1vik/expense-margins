package com.expensemargin.usersrv.controller;

import com.expensemargin.usersrv.entity.UserProfile;
import com.expensemargin.usersrv.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public UserProfile create(@RequestBody UserProfile profile) {
        return userProfileService.createProfile(profile);
    }

    @GetMapping("/{userId}")
    public UserProfile get(@PathVariable String userId) {
        return userProfileService.getByUserId(userId);
    }
}
