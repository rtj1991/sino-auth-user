package com.sino.user_auth_api.controller;

import com.sino.user_auth_api.dto.LoginHistoryDTO;
import com.sino.user_auth_api.dto.StandardResponse;
import com.sino.user_auth_api.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@Slf4j
public class LoginHistoryController {
    private final LoginHistoryService historyService;

    @GetMapping("/latest")
    public ResponseEntity<StandardResponse<List<LoginHistoryDTO>>> getLast5LoginAttempts() {
        log.info("Fetching the last 5 login attempts");

        try {
            List<LoginHistoryDTO> records = historyService.getLast5LoginAttemptDTOs();
            log.info("Successfully fetched {} login attempt(s)", records.size());

            return ResponseEntity.ok(
                    new StandardResponse<>(HttpStatus.OK.value(), "Last 5 login attempts fetched successfully", records)
            );

        } catch (Exception ex) {
            log.error("Failed to fetch login attempts: {}", ex.getMessage(), ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch login history", null)
            );
        }
    }
}
