package com.sino.user_auth_api.repository;

import com.sino.user_auth_api.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findTop5ByOrderByCreatedDesc();
}
