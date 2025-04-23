package com.sino.user_auth_api.dto;

import com.sino.user_auth_api.model.LoginHistory;

import java.time.LocalDateTime;

public record LoginHistoryDTO(LocalDateTime created,
                              String user,
                              LoginHistory.Status status) {
}
