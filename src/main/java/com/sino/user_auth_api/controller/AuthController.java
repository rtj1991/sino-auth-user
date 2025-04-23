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
        log.info("Register attempt for email: {}", req.email());

        try {
            authservice.register(req);
            log.info("User registered successfully: {}", req.email());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new StandardResponse<>(HttpStatus.CREATED.value(), "User registered successfully", null)
            );

        } catch (Exception ex) {
            log.error("Registration failed for email {}: {}", req.email(), ex.getMessage(), ex);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardResponse<>(HttpStatus.BAD_REQUEST.value(), "Registration failed", null)
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<AuthResponse>> login(@RequestBody AuthRequest req) {
        log.info("Login attempt for username: {}", req.username());

        try {
            AuthResponse login = authservice.login(req);
            log.info("User logged in successfully: {}", req.username());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new StandardResponse<>(HttpStatus.OK.value(), "User logged in successfully", login)
            );

        } catch (Exception ex) {
            log.warn("Login failed for username {}: {}", req.username(), ex.getMessage(), ex);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new StandardResponse<>(HttpStatus.UNAUTHORIZED.value(), "Login failed", null)
            );
        }
    }
}
