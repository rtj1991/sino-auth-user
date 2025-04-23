package com.sino.user_auth_api.service;

import com.sino.user_auth_api.dto.LoginHistoryDTO;
import com.sino.user_auth_api.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {
    private final LoginHistoryRepository historyRepo;

    public List<LoginHistoryDTO> getLast5LoginAttemptDTOs() {
        return historyRepo.findTop5ByOrderByCreatedDesc()
                .stream()
                .map(record -> new LoginHistoryDTO(
                        record.getCreated(),
                        record.getUser().getUsername(),
                        record.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
