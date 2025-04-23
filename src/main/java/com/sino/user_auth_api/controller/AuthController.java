package com.sino.user_auth_api.controller;

import com.sino.user_auth_api.dto.AuthRequest;
import com.sino.user_auth_api.dto.AuthResponse;
import com.sino.user_auth_api.dto.RegisterRequest;
import com.sino.user_auth_api.dto.StandardResponse;
import com.sino.user_auth_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authservice;

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<Void>> register(@RequestBody RegisterRequest req) {
        authservice.register(req);

        StandardResponse<Void> response = new StandardResponse<>(
                HttpStatus.CREATED.value(),
                "User registered successfully",
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<AuthResponse>> login(@RequestBody AuthRequest req) {
        AuthResponse login = authservice.login(req);
        StandardResponse<AuthResponse> response = new StandardResponse<>(
                HttpStatus.CREATED.value(),
                "User login successfully",
                login
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
