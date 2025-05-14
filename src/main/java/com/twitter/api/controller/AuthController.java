package com.twitter.api.controller;

import com.twitter.api.dto.LoginRequest;
import com.twitter.api.dto.LoginResponse;
import com.twitter.api.dto.RegisterRequest;
import com.twitter.api.dto.RegisterResponse;
import com.twitter.api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
