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
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

            userRepo.save(user);
            log.info("User registered successfully: {}", user.getUsername());

        } catch (DataAccessException e) {
            log.error("Database error during registration for {}: {}", req.username(), e.getMessage(), e);
            throw new RuntimeException("Database error during registration");
        } catch (Exception e) {
            log.error("Unexpected error during registration for {}: {}", req.username(), e.getMessage(), e);
            throw new RuntimeException("Registration failed due to internal error");
        }
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> {
                    log.warn("Login failed: user '{}' not found", req.username());
                    return new UsernameNotFoundException("User not found");
                });

        boolean success = encoder.matches(req.password(), user.getPassword());

        logLoginAttempt(user, success);

        if (!success) {
            log.warn("Login failed for '{}': invalid credentials", user.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        log.info("Login successful for '{}'", user.getUsername());

        return new AuthResponse(token);
    }

    private void logLoginAttempt(User user, boolean success) {
        LoginHistory history = new LoginHistory();
        history.setUser(user);
        history.setStatus(success ? LoginHistory.Status.SUCCESS : LoginHistory.Status.FAILED);

        historyRepo.save(history);
        log.info("Login history recorded for '{}': {}", user.getUsername(), history.getStatus());
    }
}
