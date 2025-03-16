package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/my")
public class MyController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CafeRepository cafeRepository;

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

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO dto) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }
}