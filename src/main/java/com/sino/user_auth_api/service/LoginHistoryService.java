package com.sino.user_auth_api.service;

import com.sino.user_auth_api.dto.LoginHistoryDTO;
import com.sino.user_auth_api.model.LoginHistory;
import com.sino.user_auth_api.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginHistoryService {
    private final LoginHistoryRepository historyRepo;

    public List<LoginHistoryDTO> getLast5LoginAttemptDTOs() {

        List<LoginHistory> last5 = historyRepo.findTop5ByOrderByCreatedDesc();

        log.debug("Fetched {} login history records", last5.size());

        return last5.stream()
                .map(record -> new LoginHistoryDTO(
                        record.getCreated(),
                        record.getUser().getUsername(),
                        record.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
