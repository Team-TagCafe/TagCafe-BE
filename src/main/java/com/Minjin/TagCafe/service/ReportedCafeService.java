package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.ReportedCafe;
import com.Minjin.TagCafe.entity.User;
import com.Minjin.TagCafe.entity.enums.CafeAttributes;
import com.Minjin.TagCafe.repository.ReportedCafeRepository;
import com.Minjin.TagCafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportedCafeService {

    private final ReportedCafeRepository reportedCafeRepository;
    private final UserRepository userRepository;

    public void reportCafe(ReportedCafe report) {
    if (report.getUser() == null) {
        throw new IllegalArgumentException("유저 객체가 포함되어야 합니다.");
    }
    if (report.getUser().getUserEmail() == null || report.getUser().getUserEmail().isBlank()) {
        throw new IllegalArgumentException("userEmail이 누락되었거나 비어있습니다.");
    }
    String email = report.getUser().getUserEmail(); // JSON에서 유저의 이메일만 넘어오면 OK
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        report.setUser(user); // DB 연동된 진짜 user 객체로 대체
        report.setCreatedAt(LocalDateTime.now());
        report.setApproved(false);
        reportedCafeRepository.save(report);
    }

    public List<ReportedCafe> getReportsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return reportedCafeRepository.findByUser(user);
    }
}
