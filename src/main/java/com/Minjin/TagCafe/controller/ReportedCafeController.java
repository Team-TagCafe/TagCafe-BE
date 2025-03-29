package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.PhotoUrlsRequest;
import com.Minjin.TagCafe.dto.ReportedCafeDTO;
import com.Minjin.TagCafe.entity.*;
import com.Minjin.TagCafe.repository.ReportedCafeRepository;
import com.Minjin.TagCafe.repository.ReviewRepository;
import com.Minjin.TagCafe.repository.UserRepository;
import com.Minjin.TagCafe.service.CafeService;
import com.Minjin.TagCafe.service.ReportedCafeService;
import com.Minjin.TagCafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportedCafeController {

    private final ReportedCafeService reportedCafeService;
    private final ReportedCafeRepository reportedCafeRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CafeService cafeService;

    @PostMapping
    public ResponseEntity<String> reportCafe(@RequestBody ReportedCafe reportedCafe) {
        reportedCafeService.reportCafe(reportedCafe);
        return ResponseEntity.ok("제보 완료");
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<ReportedCafeDTO>> getReportsByUser(@PathVariable("userEmail") String userEmail) {
        List<ReportedCafe> reports = reportedCafeService.getReportsByUser(userEmail);
        List<ReportedCafeDTO> dtos = reports.stream()
                .map(ReportedCafeDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
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
    public ResponseEntity<?> getCafeByKakaoPlaceId(@PathVariable("kakaoPlaceId") Long kakaoPlaceId) {
        return cafeRepository.findByKakaoPlaceId(kakaoPlaceId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().body(Map.of("exists", false)));
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
    public ResponseEntity<List<ReportedCafeDTO>> getAllReports() {
        List<ReportedCafe> reports = reportedCafeRepository.findAll();
        List<ReportedCafeDTO> dtos = reports.stream()
                .map(ReportedCafeDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }
    //제보 승인
    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<String> approveReport(@PathVariable("id") Long id,
                                                @RequestBody(required = false) PhotoUrlsRequest photoUrlsRequest) {
        ReportedCafe report = reportedCafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제보가 존재하지 않습니다."));
        report.setStatus(ReportedCafe.ReportStatus.APPROVED);

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

        // 이미지 저장
        if (photoUrlsRequest != null && photoUrlsRequest.getPhotoUrls() != null) {
            List<CafeImage> images = photoUrlsRequest.getPhotoUrls().stream().limit(5)
                    .map(url -> {
                        byte[] imageData = cafeService.fetchImageAsBytes(url);
                        return CafeImage.builder()
                                .imageData(imageData)
                                .cafe(cafe)
                                .build();
                    }).collect(Collectors.toList());

            cafe.setImages(images);
        }

        cafeRepository.save(cafe);
        report.setCafe(cafe);
        reportedCafeRepository.save(report);

        User user = userRepository.findByEmail(report.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        Review review = new Review();
        review.setCafe(cafe);
        review.setUserEmail(user.getUserEmail());
        review.setContent(report.getContent());
        review.setCreatedAt(LocalDateTime.now());
        review.setWifi(report.getWifi());
        review.setOutlets(report.getOutlets());
        review.setDesk(report.getDesk());
        review.setRestroom(report.getRestroom());
        review.setParking(report.getParking());
        review.setRating(report.getRating());

        reviewRepository.save(review);

        Double avgRating = reviewRepository.findAverageRatingByCafe(cafe);
        if (avgRating != null) {
            cafe.setAverageRating(avgRating);
            cafeRepository.save(cafe);
        }

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
