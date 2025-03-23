package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Review;
import com.Minjin.TagCafe.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReview(@RequestBody ReviewDTO reviewDTO) {
        System.out.println("Received DTO:"+ reviewDTO);

        if (reviewDTO.getUserEmail() == null || reviewDTO.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("userEmail이 필요합니다.");
        }
        reviewService.saveReview(reviewDTO);
        return ResponseEntity.ok("리뷰가 저장되었습니다.");
    }

    @GetMapping("/{cafeId}")
    public ResponseEntity<List<Review>> getReviewsByCafe(@PathVariable("cafeId") Long cafeId) {
        return ResponseEntity.ok(reviewService.getReviewsByCafeId(cafeId));
    }
}