package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.entity.ReportedCafe;
import com.Minjin.TagCafe.repository.ReportedCafeRepository;
import com.Minjin.TagCafe.service.ReportedCafeService;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.repository.CafeRepository;
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
    private final CafeRepository cafeRepository;

    @PostMapping
    public ResponseEntity<String> reportCafe(@RequestBody ReportedCafe reportedCafe) {
        reportedCafeService.reportCafe(reportedCafe);
        return ResponseEntity.ok("제보 완료");
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<ReportedCafe>> getReportsByUser(@PathVariable("userEmail") String userEmail) {
        List<ReportedCafe> reports = reportedCafeService.getReportsByUser(userEmail);
        return ResponseEntity.ok(reports);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReportedCafe> getReportedCafe(@PathVariable("id") Long id) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        return ResponseEntity.ok(report);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateReportedCafe(@PathVariable("id") Long id, @RequestBody ReportedCafe updated) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));

        // 업데이트할 필드 설정
        report.setWifi(updated.getWifi());
        report.setOutlets(updated.getOutlets());
        report.setDesk(updated.getDesk());
        report.setRestroom(updated.getRestroom());
        report.setParking(updated.getParking());
        report.setContent(updated.getContent());

        reportedCafeRepository.save(report);
        return ResponseEntity.ok("제보가 수정되었습니다.");
    }

    //이미있는 카페인지 확인
    @GetMapping("/cafes/kakao/{kakaoPlaceId}")
    public ResponseEntity<Cafe> getCafeByKakaoPlaceId(@PathVariable("kakaoPlaceId") Long kakaoPlaceId) {
        return cafeRepository.findByKakaoPlaceId(kakaoPlaceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    //관리자기능
    //미승인 제보목록조회
    @GetMapping("/admin/pending")
    public ResponseEntity<List<ReportedCafe>> getPendingReports() {
        List<ReportedCafe> pending = reportedCafeRepository.findByStatus(ReportedCafe.ReportStatus.PENDING);
        return ResponseEntity.ok(pending);
    }

    // 모든 제보 목록 조회
    @GetMapping("/admin/all")
    public ResponseEntity<List<ReportedCafe>> getAllReports() {
        List<ReportedCafe> allReports = reportedCafeRepository.findAll();
        return ResponseEntity.ok(allReports);
    }
    //제보 승인
    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<String> approveReport(@PathVariable("id") Long id) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        report.setStatus(ReportedCafe.ReportStatus.APPROVED);
        reportedCafeRepository.save(report);

        Cafe cafe = new Cafe();
        cafe.setCafeName(report.getCafeName());
        cafe.setAddress(report.getAddress());
        cafe.setLatitude(report.getLatitude());
        cafe.setLongitude(report.getLongitude());
        cafe.setPhoneNumber(report.getPhoneNumber());
        cafe.setWebsiteUrl(report.getWebsiteUrl());
        cafe.setOpeningHours(report.getOpeningHours());
        cafe.setWifi(report.getWifi());
        cafe.setOutlets(report.getOutlets());
        cafe.setDesk(report.getDesk());
        cafe.setRestroom(report.getRestroom());
        cafe.setParking(report.getParking());
        cafe.setKakaoPlaceId(Long.valueOf(report.getKakaoPlaceId()));
        cafeRepository.save(cafe);

        report.setCafe(cafe); // Cafe 객체를 ReportedCafe에 연결
        reportedCafeRepository.save(report); // 다시 저장

        return ResponseEntity.ok("승인되었습니다.");
    }

    // 제보 상세 조회
    @GetMapping("/admin/pending/{id}")
    public ResponseEntity<ReportedCafe> getPendingReportDetail(@PathVariable("id") Long id) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        return ResponseEntity.ok(report);
    }

    // 제보 삭제
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable("id") Long id) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        report.setStatus(ReportedCafe.ReportStatus.REJECTED);
        reportedCafeRepository.save(report);
        return ResponseEntity.ok("반려 처리되었습니다.");
    }

}
