package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.ReportedCafe;
import com.Minjin.TagCafe.entity.User;
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
        if (report.getUserEmail() == null || report.getUserEmail().isBlank()) {
            throw new IllegalArgumentException("userEmail이 누락되었거나 비어있습니다.");
        }
        report.setCreatedAt(LocalDateTime.now());
        report.setStatus(ReportedCafe.ReportStatus.PENDING);
        reportedCafeRepository.save(report);
    }

    public List<ReportedCafe> getReportsByUser(String userEmail) {
        return reportedCafeRepository.findByUserEmail(userEmail);
    }
}
