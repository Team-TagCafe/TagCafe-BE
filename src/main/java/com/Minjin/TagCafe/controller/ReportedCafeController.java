package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.entity.ReportedCafe;
import com.Minjin.TagCafe.repository.ReportedCafeRepository;
import com.Minjin.TagCafe.service.ReportedCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportedCafeController {

    private final ReportedCafeService reportedCafeService;
    private final ReportedCafeRepository reportedCafeRepository;

    @PostMapping
    public ResponseEntity<String> reportCafe(@RequestBody ReportedCafe reportedCafe) {
        reportedCafeService.reportCafe(reportedCafe);
        return ResponseEntity.ok("제보 완료");
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<List<ReportedCafe>> getReportsByUser(@PathVariable("userEmail") String userEmail) {
        List<ReportedCafe> reports = reportedCafeService.getReportsByUser(userEmail);
        return ResponseEntity.ok(reports);
    }


    //관리자기능
    //미승인 제보목록조회
    @GetMapping("/admin/pending")
    public ResponseEntity<List<ReportedCafe>> getPendingReports() {
        List<ReportedCafe> pending = reportedCafeRepository.findByApprovedFalse();
        return ResponseEntity.ok(pending);
    }
    //제보 승인처리
    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<String> approveReport(@PathVariable Long id) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        report.setApproved(true);
        reportedCafeRepository.save(report);
        return ResponseEntity.ok("승인되었습니다.");
    }

    // 제보 상세 조회
    @GetMapping("/admin/pending/{id}")
    public ResponseEntity<ReportedCafe> getPendingReportDetail(@PathVariable Long id) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        return ResponseEntity.ok(report);
    }
}
