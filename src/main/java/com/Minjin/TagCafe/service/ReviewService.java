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