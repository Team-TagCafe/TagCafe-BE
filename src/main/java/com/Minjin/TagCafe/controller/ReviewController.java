package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Review;
import com.Minjin.TagCafe.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review", description = "카페 리뷰 작성, 조회, 수정, 삭제 관련 API")
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 작성", description = "카페에 대한 새로운 리뷰를 작성합니다.")
    @PostMapping("/create")
    public ResponseEntity<String> createReview(@RequestBody ReviewDTO reviewDTO) {
        System.out.println("Received DTO:"+ reviewDTO);

        if (reviewDTO.getUserEmail() == null || reviewDTO.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("userEmail이 필요합니다.");
        }
        reviewService.saveReview(reviewDTO);
        return ResponseEntity.ok("리뷰가 저장되었습니다.");
    }

    @Operation(summary = "카페별 리뷰 조회", description = "특정 카페에 작성된 모든 리뷰를 조회합니다.")
    @GetMapping("/{cafeId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByCafe(@PathVariable("cafeId") Long cafeId) {
        return ResponseEntity.ok(reviewService.getReviewsByCafeId(cafeId));
    }
}