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
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository, CafeRepository cafeRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.cafeRepository = cafeRepository;
        this.userService = userService;
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

        Double avgRating = reviewRepository.findAverageRatingByCafe(cafe);
        if (avgRating != null) {
            cafe.setAverageRating(avgRating);
            cafeRepository.save(cafe);
        }

        updateCafeTags(cafe);
    }

    public List<ReviewDTO> getReviewsByCafeId(Long cafeId) {
        List<Review> reviews = reviewRepository.findByCafe_CafeId(cafeId);

        return reviews.stream().map(review -> {
            String nickname = userService.getNicknameByEmail(review.getUserEmail());
            return new ReviewDTO(review, nickname);
        }).collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUser(String userEmail) {
        List<Review> reviews = reviewRepository.findByUserEmail(userEmail);
        String nickname = userService.getNicknameByEmail(userEmail);

        return reviews.stream().map(review -> {
            Cafe cafe = review.getCafe();  //Review에서 직접 Cafe 객체 가져오기
            String cafeName = (cafe != null) ? cafe.getCafeName() : "알 수 없는 카페";

            ReviewDTO dto = new ReviewDTO(review, nickname);
            dto.setCafeName(cafeName);
            return dto;
        }).collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));
        String nickname = userService.getNicknameByEmail(review.getUserEmail());
        return new ReviewDTO(review, nickname);
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

        Cafe cafe = review.getCafe();
        Double avgRating = reviewRepository.findAverageRatingByCafe(cafe);
        if (avgRating != null) {
            cafe.setAverageRating(avgRating);
            cafeRepository.save(cafe);
        }

        updateCafeTags(cafe);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        Cafe cafe = review.getCafe();
        reviewRepository.deleteById(reviewId);

        Double avgRating = reviewRepository.findAverageRatingByCafe(cafe);
        cafe.setAverageRating(avgRating != null ? avgRating : 0.0); // 리뷰 전부 삭제되었을 경우 0.0으로 처리
        cafeRepository.save(cafe);

        updateCafeTags(cafe);
    }

    // 태그 최빈값 계산
    private <T> T getMostFrequentValue(List<T> values) {
        return values.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // 없으면 null
    }

    // Cafe 태그값 업데이트
    private void updateCafeTags(Cafe cafe) {
        List<Review> reviews = reviewRepository.findByCafe_CafeId(cafe.getCafeId());

        if (reviews.isEmpty()) return;

        cafe.setWifi(getMostFrequentValue(
                reviews.stream().map(Review::getWifi).collect(Collectors.toList()))
        );
        cafe.setOutlets(getMostFrequentValue(
                reviews.stream().map(Review::getOutlets).collect(Collectors.toList()))
        );
        cafe.setDesk(getMostFrequentValue(
                reviews.stream().map(Review::getDesk).collect(Collectors.toList()))
        );
        cafe.setRestroom(getMostFrequentValue(
                reviews.stream().map(Review::getRestroom).collect(Collectors.toList()))
        );
        cafe.setParking(getMostFrequentValue(
                reviews.stream().map(Review::getParking).collect(Collectors.toList()))
        );

        cafeRepository.save(cafe);
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
            case "주차" ->
                    review.getParking() != null && getParkingDisplayValue(review.getParking().name()).equals(value);
            default -> false;
        };
    }


    private String getParkingDisplayValue(String rawValue) {
        return switch (rawValue) {
            case "가능_무료" -> "가능(무료)";
            case "가능_유료" -> "가능(유료)";
            case "가능_일부" -> "가능(일부)";
            case "불가능" -> "불가능";
            default -> rawValue;
        };
    }
}