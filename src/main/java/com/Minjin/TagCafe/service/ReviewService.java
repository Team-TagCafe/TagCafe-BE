package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.dto.ReviewDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.Review;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 특정 카페의 모든 리뷰에서 태그 빈도 분석

    public Map<String, Long> getCafeTagStatistics(Long cafeId) {
        List<Review> reviews = reviewRepository.findByCafeId(cafeId);

        Map<String, Long> tagCount = new HashMap<>();

        tagCount.put("wifi_빠름", reviews.stream().filter(r -> r.getWifi() == WifiSpeed.빠름).count());
        tagCount.put("wifi_보통", reviews.stream().filter(r -> r.getWifi() == WifiSpeed.보통).count());
        tagCount.put("wifi_없음", reviews.stream().filter(r -> r.getWifi() == WifiSpeed.없음).count());

        tagCount.put("outlets_자리마다", reviews.stream().filter(r -> r.getOutlets() == OutletAvailability.자리마다).count());
        tagCount.put("outlets_일부", reviews.stream().filter(r -> r.getOutlets() == OutletAvailability.일부).count());
        tagCount.put("outlets_없음", reviews.stream().filter(r -> r.getOutlets() == OutletAvailability.없음).count());

        tagCount.put("desk_넓음", reviews.stream().filter(r -> r.getDesk() == DeskSize.넓음).count());
        tagCount.put("desk_적당함", reviews.stream().filter(r -> r.getDesk() == DeskSize.적당함).count());
        tagCount.put("desk_좁음", reviews.stream().filter(r -> r.getDesk() == DeskSize.좁음).count());

        tagCount.put("restroom_가능", reviews.stream().filter(r -> r.getRestroom() == RestroomAvailability.가능).count());
        tagCount.put("restroom_불가능", reviews.stream().filter(r -> r.getRestroom() == RestroomAvailability.불가능).count());

        tagCount.put("parking_가능_무료", reviews.stream().filter(r -> r.getParking() == ParkingAvailability.가능_무료).count());
        tagCount.put("parking_가능_유료", reviews.stream().filter(r -> r.getParking() == ParkingAvailability.가능_유료).count());
        tagCount.put("parking_가능_일부제공", reviews.stream().filter(r -> r.getParking() == ParkingAvailability.가능_일부제공).count());
        tagCount.put("parking_불가능", reviews.stream().filter(r -> r.getParking() == ParkingAvailability.불가능).count());

        return tagCount;
    }
     */
}