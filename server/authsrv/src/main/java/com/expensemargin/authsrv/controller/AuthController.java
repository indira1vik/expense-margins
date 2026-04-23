package com.expensemargin.authsrv.controller;

import com.expensemargin.authsrv.dto.AuthResponse;
import com.expensemargin.authsrv.dto.LoginRequest;
import com.expensemargin.authsrv.dto.RegisterRequest;
import com.expensemargin.authsrv.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
