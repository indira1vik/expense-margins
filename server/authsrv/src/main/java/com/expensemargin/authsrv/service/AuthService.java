package com.expensemargin.authsrv.service;

import com.expensemargin.authsrv.dto.AuthResponse;
import com.expensemargin.authsrv.dto.LoginRequest;
import com.expensemargin.authsrv.dto.RegisterRequest;
import com.expensemargin.authsrv.dto.UserProfileSyncRequest;
import com.expensemargin.authsrv.entity.User;
import com.expensemargin.authsrv.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Service
public class AuthService {
    private static final String ACTIVE_STATUS = "ACTIVE";

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RestClient usersClient;

    public AuthService(UserRepo userRepo,
                       PasswordEncoder passwordEncoder,
                       @Value("${usersrv.base-url:http://localhost:8082}") String usersBaseUrl) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.usersClient = RestClient.builder().baseUrl(usersBaseUrl).build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User existingUser = userRepo.findByEmail(request.getEmail()).orElse(null);
        if (existingUser != null) {
            return new AuthResponse("User already exist!", existingUser.getUserid());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String userId = UUID.randomUUID().toString();
        user.setUserid(userId);

        userRepo.save(user);
        syncUserProfile(user);

        return new AuthResponse("Registration successful", user.getUserid());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid credentials", user.getUserid());
        }

        return new AuthResponse("Login Successful", user.getUserid());
    }

    private void syncUserProfile(User user) {
        UserProfileSyncRequest request = new UserProfileSyncRequest(
                user.getUserid(),
                user.getName(),
                user.getEmail(),
                "",
                ACTIVE_STATUS
        );

        try {
            usersClient.post()
                    .uri("/users")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            throw new RuntimeException("Failed to sync user profile to usersrv", ex);
        }
    }
}
