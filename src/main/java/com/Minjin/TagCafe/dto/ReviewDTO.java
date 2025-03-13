package com.Minjin.TagCafe.dto;

import com.Minjin.TagCafe.entity.*;
import com.Minjin.TagCafe.entity.enums.CafeAttributes.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDTO {
    private Long cafeId;
    private String userEmail;
    private int rating;
    private String content;

    private WifiSpeed wifi;
    private OutletAvailability outlets;
    private DeskSize desk;
    private RestroomAvailability restroom;
    private ParkingAvailability parking;
}