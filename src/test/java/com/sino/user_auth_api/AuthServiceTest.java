package com.sino.user_auth_api;

import com.sino.user_auth_api.dto.AuthRequest;
import com.sino.user_auth_api.dto.AuthResponse;
import com.sino.user_auth_api.dto.RegisterRequest;
import com.sino.user_auth_api.model.LoginHistory;
import com.sino.user_auth_api.model.User;
import com.sino.user_auth_api.repository.LoginHistoryRepository;
import com.sino.user_auth_api.repository.UserRepository;
import com.sino.user_auth_api.service.AuthService;
import com.sino.user_auth_api.service.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    private UserRepository userRepo;

    private LoginHistoryRepository historyRepo;

    private PasswordEncoder encoder;

    private JwtService jwtService;

    private AuthService authService;

    @Before
    public void setUp() {
        historyRepo = mock(LoginHistoryRepository.class);
        userRepo = mock(UserRepository.class);
        encoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        authService = new AuthService(userRepo, historyRepo, encoder, jwtService);
    }

    @Test
    public void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("user1", "user1@example.com", "password123");

        when(encoder.encode("password123")).thenReturn("encodedPassword");

        User mockUser = new User();
        when(userRepo.save(any(User.class))).thenReturn(mockUser);

        authService.register(request);

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    public void testLogin_Successful() {
        AuthRequest request = new AuthRequest("user1", "password123");

        User mockUser = new User();
        mockUser.setUsername("user1");
        mockUser.setPassword("encodedPassword");

        when(userRepo.findByUsername("user1")).thenReturn(java.util.Optional.of(mockUser));
        when(encoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("user1")).thenReturn("mock-jwt");

        LoginHistory mockHistory = new LoginHistory();
        mockHistory.setStatus(LoginHistory.Status.SUCCESS);
        mockHistory.setCreated(java.time.LocalDateTime.now());
        mockHistory.setUser(mockUser);
        when(historyRepo.save(any(LoginHistory.class))).thenReturn(mockHistory);

        AuthResponse response = authService.login(request);

        assertEquals("mock-jwt", response.token());
        verify(userRepo, times(1)).findByUsername("user1");
        verify(historyRepo, times(1)).save(any(LoginHistory.class));
    }

    @Test(expected = org.springframework.security.authentication.BadCredentialsException.class)
    public void testLogin_Failed_InvalidPassword() {
        AuthRequest request = new AuthRequest("user1", "wrongPassword");

        User mockUser = new User();
        mockUser.setUsername("user1");
        mockUser.setPassword("encodedPassword");

        when(userRepo.findByUsername("user1")).thenReturn(java.util.Optional.of(mockUser));
        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        authService.login(request);
    }

    @Test(expected = org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    public void testLogin_Failed_UserNotFound() {
        AuthRequest request = new AuthRequest("nonexistentUser", "password123");

        when(userRepo.findByUsername("nonexistentUser")).thenReturn(java.util.Optional.empty());

        authService.login(request);
    }
}
