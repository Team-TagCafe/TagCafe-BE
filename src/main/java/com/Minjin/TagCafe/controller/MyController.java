package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "My", description = "마이페이지 - 내가 작성한 리뷰 조회 및 관리 API")
@RestController
@RequestMapping("/my")
public class MyController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CafeRepository cafeRepository;

    @Operation(summary = "내가 작성한 리뷰 목록 조회", description = "userEmail을 기반으로 사용자가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getUserReviews(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "userEmail") String userEmail) {


        List<ReviewDTO> reviews = reviewService.getReviewsByUser(userEmail);

        //디버깅용 로그
//        System.out.println("조회된 리뷰 개수: " + reviews.size()); // 🔹 조회된 리뷰 개수 확인
//        for (ReviewDTO review : reviews) {
//            System.out.println("리뷰 내용: " + review); // 🔹 실제 리뷰 데이터 확인
//        }

        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 ID를 기반으로 해당 리뷰의 상세 정보와 카페 정보를 조회합니다.")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Map<String, Object>> getReviewById(@PathVariable("reviewId") Long reviewId) {
        if (reviewId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            ReviewDTO reviewDTO = reviewService.getReviewById(reviewId); // 리뷰 정보 조회
            Long cafeId = reviewDTO.getCafeId();

            // ✅ 카페 정보 추가 조회
            Cafe cafe = cafeRepository.findById(cafeId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 카페가 존재하지 않습니다."));

            // ✅ 리뷰 + 카페 정보를 함께 반환
            Map<String, Object> response = new HashMap<>();
            response.put("review", reviewDTO);
            response.put("cafeName", cafe.getCafeName());
            response.put("cafeAddress", cafe.getAddress());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 ID를 기반으로 내용을 수정합니다.")
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewDTO dto) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 ID를 기반으로 리뷰를 삭제합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 실패");
        }
    }

    @Operation(summary = "태그 기반 리뷰 필터 조회", description = "userEmail과 선택한 태그 조건을 바탕으로 리뷰를 필터링하여 조회합니다.")
    @GetMapping("/reviews/filter")
    public ResponseEntity<List<ReviewDTO>> getReviewsByCafeWithFilter(
            @RequestParam("userEmail") String userEmail,
            @RequestParam(value = "tags", required = false) List<String> tagNames,
            @RequestParam(value = "values", required = false) List<String> values) {

        return ResponseEntity.ok(reviewService.getReviewsWithFilter(userEmail, tagNames, values));
    }


}