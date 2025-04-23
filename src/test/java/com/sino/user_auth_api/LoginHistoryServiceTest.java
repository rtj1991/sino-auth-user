package com.sino.user_auth_api;

import com.sino.user_auth_api.dto.LoginHistoryDTO;
import com.sino.user_auth_api.model.LoginHistory;
import com.sino.user_auth_api.model.User;
import com.sino.user_auth_api.repository.LoginHistoryRepository;
import com.sino.user_auth_api.service.LoginHistoryService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoginHistoryServiceTest {

    private LoginHistoryRepository loginHistoryRepository;
    private LoginHistoryService loginHistoryService;

    @Before
    public void setUp() {
        loginHistoryRepository = mock(LoginHistoryRepository.class);
        loginHistoryService = new LoginHistoryService(loginHistoryRepository);
    }

    @Test
    public void testGetLast5LoginAttempts() {
        List<LoginHistory> loginHistoryList = new ArrayList<>();

        User mockUser = new User();
        mockUser.setUsername("test_user");

        LoginHistory loginHistory1 = new LoginHistory();
        loginHistory1.setCreated(LocalDateTime.now());
        loginHistory1.setStatus(LoginHistory.Status.SUCCESS);
        loginHistory1.setUser(mockUser);

        LoginHistory loginHistory2 = new LoginHistory();
        loginHistory2.setCreated(LocalDateTime.now().minusMinutes(10));
        loginHistory2.setStatus(LoginHistory.Status.FAILED);
        loginHistory2.setUser(mockUser);

        loginHistoryList.add(loginHistory1);
        loginHistoryList.add(loginHistory2);

        when(loginHistoryRepository.findTop5ByOrderByCreatedDesc()).thenReturn(loginHistoryList);

        List<LoginHistoryDTO> result = loginHistoryService.getLast5LoginAttemptDTOs();
        assertEquals(loginHistoryList.size(), result.size());
    }

    @Test
    public void testGetLast5LoginAttempts_EmptyList() {
        when(loginHistoryRepository.findTop5ByOrderByCreatedDesc()).thenReturn(new ArrayList<>());

        List<LoginHistoryDTO> result = loginHistoryService.getLast5LoginAttemptDTOs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
