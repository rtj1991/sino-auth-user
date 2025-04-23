package com.sino.user_auth_api.service;

import com.sino.user_auth_api.dto.AuthRequest;
import com.sino.user_auth_api.dto.AuthResponse;
import com.sino.user_auth_api.dto.RegisterRequest;
import com.sino.user_auth_api.model.LoginHistory;
import com.sino.user_auth_api.model.User;
import com.sino.user_auth_api.repository.LoginHistoryRepository;
import com.sino.user_auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final LoginHistoryRepository historyRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public void register(RegisterRequest req) {
        try {
            User user = new User();
            user.setUsername(req.username());
            user.setEmail(req.email());
            user.setPassword(encoder.encode(req.password()));
            user.setCreated(LocalDateTime.now());
            userRepo.save(user);
            log.info("User registered: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage(), e);
            throw new RuntimeException("Registration failed");
        }
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> {
                    log.warn("Login failed: user '{}' not found", req.username());
                    return new UsernameNotFoundException("User not found");
                });

        boolean success = encoder.matches(req.password(), user.getPassword());

        LoginHistory history = new LoginHistory();
        history.setStatus(success ? LoginHistory.Status.SUCCESS : LoginHistory.Status.FAILED);
        history.setCreated(LocalDateTime.now());
        history.setUser(user);
        user.getLoginHistories().add(history);
        historyRepo.save(history);

        log.info("Login attempt for '{}': {}", user.getUsername(), success ? LoginHistory.Status.SUCCESS : LoginHistory.Status.FAILED);

        if (!success) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
