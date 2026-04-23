package com.expensemargin.usersrv.service;

import com.expensemargin.usersrv.entity.UserProfile;
import com.expensemargin.usersrv.repository.UserProfileRepo;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private final UserProfileRepo userProfileRepo;

    public UserProfileService(UserProfileRepo userProfileRepo) {
        this.userProfileRepo = userProfileRepo;
    }

    public UserProfile createProfile(UserProfile profile) {
        return userProfileRepo.save(profile);
    }

    public UserProfile getByUserId(String userId) {
        return userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
