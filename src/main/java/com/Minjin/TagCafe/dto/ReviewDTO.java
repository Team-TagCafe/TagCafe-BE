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
    private Long cafeId;
    private String cafeName;
    private String userEmail;
    private int rating;
    private String content;

    private WifiSpeed wifi;
    private OutletAvailability outlets;
    private DeskSize desk;
    private RestroomAvailability restroom;
    private ParkingAvailability parking;

    private LocalDateTime createdAt;
}