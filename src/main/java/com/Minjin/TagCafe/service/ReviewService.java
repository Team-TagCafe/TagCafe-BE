package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Review;
import com.Minjin.TagCafe.entity.Tag;
import com.Minjin.TagCafe.repository.ReviewRepository;
import com.Minjin.TagCafe.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TagRepository tagRepository;

    public ReviewService(ReviewRepository reviewRepository, TagRepository tagRepository) {
        this.reviewRepository = reviewRepository;
        this.tagRepository = tagRepository;
    }

    public Review saveReview(ReviewDTO reviewDTO) {
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 태그 ID를 실제 Tag 엔티티로 변환
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(reviewDTO.getTagIds()));

        Review review = new Review(
                reviewDTO.getCafeId(),
                reviewDTO.getUserEmail(),
                reviewDTO.getRating(),
                reviewDTO.getContent(),
                tags, // 실제 태그 엔티티 전달
                createdAt
        );

        return reviewRepository.save(review);
    }
}