package com.sino.user_auth_api.controller;

import com.sino.user_auth_api.dto.LoginHistoryDTO;
import com.sino.user_auth_api.dto.StandardResponse;
import com.sino.user_auth_api.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class LoginHistoryController {
    private final LoginHistoryService historyService;

    @GetMapping("/latest")
    public ResponseEntity<StandardResponse<List<LoginHistoryDTO>>> getLast5LoginAttempts() {
        List<LoginHistoryDTO> records = historyService.getLast5LoginAttemptDTOs();

        StandardResponse<List<LoginHistoryDTO>> response = new StandardResponse<>(
                HttpStatus.OK.value(),
                "Last 5 login attempts fetched successfully",
                records
        );

        return ResponseEntity.ok(response);
    }
}
