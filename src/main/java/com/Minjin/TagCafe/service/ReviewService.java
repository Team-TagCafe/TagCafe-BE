package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.Review;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;

    public ReviewService(ReviewRepository reviewRepository, CafeRepository cafeRepository) {
        this.reviewRepository = reviewRepository;
        this.cafeRepository = cafeRepository;
    }

    @Transactional
    public void saveReview(ReviewDTO dto) {
        Cafe cafe = cafeRepository.findById(dto.getCafeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        Review review = Review.builder()
                .cafe(cafe)
                .userEmail(dto.getUserEmail())
                .rating(dto.getRating())
                .content(dto.getContent())
                .wifi(dto.getWifi())
                .outlets(dto.getOutlets())
                .desk(dto.getDesk())
                .restroom(dto.getRestroom())
                .parking(dto.getParking())
                .build();

        reviewRepository.save(review);
    }

    public List<Review> getReviewsByCafeId(Long cafeId) {
        return reviewRepository.findByCafe_CafeId(cafeId);
    }

    public List<ReviewDTO> getReviewsByUser(String userEmail) {
        List<Review> reviews = reviewRepository.findByUserEmail(userEmail);

        return reviews.stream().map(review -> {
            Cafe cafe = review.getCafe();  //Review에서 직접 Cafe 객체 가져오기
            String cafeName = (cafe != null) ? cafe.getCafeName() : "알 수 없는 카페";

            return new ReviewDTO(
                    review.getId(),
                    review.getCafe().getCafeId(),
                    cafeName,
                    review.getUserEmail(),
                    review.getRating(),
                    review.getContent(),
                    review.getWifi(),
                    review.getOutlets(),
                    review.getDesk(),
                    review.getRestroom(),
                    review.getParking(),
                    review.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));
        return new ReviewDTO(review);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewDTO dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setWifi(dto.getWifi());
        review.setOutlets(dto.getOutlets());
        review.setDesk(dto.getDesk());
        review.setRestroom(dto.getRestroom());
        review.setParking(dto.getParking());

        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("해당 리뷰가 존재하지 않습니다.");
        }
        reviewRepository.deleteById(reviewId);
    }


    public List<ReviewDTO> getReviewsWithFilter(String userEmail, List<String> tagNames, List<String> values) {
        List<Review> reviews = reviewRepository.findByUserEmail(userEmail);

        if (tagNames == null) tagNames = new ArrayList<>();
        if (values == null) values = new ArrayList<>();

        // 🔹 평점 필터 적용
        boolean hasGradeFilter = tagNames.contains("평점");
        if (hasGradeFilter) {
            int index = tagNames.indexOf("평점");
            String gradeFilter = values.get(index);

            final double minGrade = switch (gradeFilter) {
                case "5.0" -> 5.0;
                case "4.0 이상" -> 4.0;
                case "3.0 이상" -> 3.0;
                default -> 0.0;
            };

            tagNames.remove(index);
            values.remove(index);

            reviews = reviews.stream()
                    .filter(review -> review.getRating() >= minGrade)
                    .collect(Collectors.toList());
        }

        // 🔹 카페 옵션(와이파이, 콘센트 등) 필터 적용
        for (int i = 0; i < tagNames.size(); i++) {
            String tagName = tagNames.get(i);
            String value = values.get(i);

            final String filterValue = value;
            reviews = reviews.stream()
                    .filter(review -> reviewMatchesTag(review, tagName, filterValue))
                    .collect(Collectors.toList());
        }

        return reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

    private boolean reviewMatchesTag(Review review, String tagName, String value) {
        if (value == null || tagName == null) return false; // 값이 없으면 필터링 불가능

        return switch (tagName) {
            case "와이파이" -> review.getWifi() != null && review.getWifi().name().equals(value);
            case "콘센트" -> review.getOutlets() != null && review.getOutlets().name().equals(value);
            case "책상" -> review.getDesk() != null && review.getDesk().name().equals(value);
            case "화장실" -> review.getRestroom() != null && review.getRestroom().name().equals(value);
            case "주차" -> review.getParking() != null && review.getParking().name().equals(value);
            default -> false;
        };
    }
}