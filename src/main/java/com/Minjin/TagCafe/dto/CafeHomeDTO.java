package com.Minjin.TagCafe.dto;

import com.Minjin.TagCafe.entity.enums.CafeAttributes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CafeHomeDTO {
    private Long cafeId;
    private String cafeName;
    private double latitude;
    private double longitude;
    private String address;
    private double averageRating;
    private String openingHours;

    private CafeAttributes.WifiSpeed wifi;
    private CafeAttributes.OutletAvailability outlets;
    private CafeAttributes.DeskSize desk;
    private CafeAttributes.RestroomAvailability restroom;
    private CafeAttributes.ParkingAvailability parking;

    private String thumbnailImageBase64;  // 대표 이미지 1장
}
