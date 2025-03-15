package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my")
public class MyController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getUserReviews(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "userEmail") String userEmail) {

        System.out.println("Authorization Header: " + authorizationHeader);
        System.out.println("요청된 userEmail: " + userEmail);

        List<ReviewDTO> reviews = reviewService.getReviewsByUser(userEmail);

        System.out.println("조회된 리뷰 개수: " + reviews.size()); // 🔹 조회된 리뷰 개수 확인
        for (ReviewDTO review : reviews) {
            System.out.println("리뷰 내용: " + review); // 🔹 실제 리뷰 데이터 확인
        }

        return ResponseEntity.ok(reviews);
    }
}