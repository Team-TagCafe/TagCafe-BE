package com.Minjin.TagCafe.dto;

import com.Minjin.TagCafe.entity.*;
import com.Minjin.TagCafe.entity.enums.CafeAttributes.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewId;
    private Long cafeId;
    private String cafeName;
    private String userEmail;
    private String userNickname;
    private int rating;
    private String content;
    private String thumbnailImageUrl;

    private WifiSpeed wifi;
    private OutletAvailability outlets;
    private DeskSize desk;
    private RestroomAvailability restroom;
    private ParkingAvailability parking;

    private LocalDateTime createdAt;

    // Review 객체를 받을 수 있도록 생성자 추가
    public ReviewDTO(Review review, String nickname) {
        this.reviewId=review.getId();
        this.cafeId = review.getCafe().getCafeId();
        this.cafeName = review.getCafe().getCafeName();
        this.userEmail = review.getUserEmail();
        this.userNickname = nickname;
        this.rating = review.getRating();
        this.content = review.getContent();
        this.wifi = review.getWifi();
        this.outlets = review.getOutlets();
        this.desk = review.getDesk();
        this.restroom = review.getRestroom();
        this.parking = review.getParking();
        this.createdAt = review.getCreatedAt();

        Cafe cafe = review.getCafe();
        this.thumbnailImageUrl = (cafe.getImages() != null && !cafe.getImages().isEmpty())
                ? cafe.getImages().get(0).getImageUrl()
                : null;
    }
}
