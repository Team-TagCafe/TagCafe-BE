package com.Minjin.TagCafe.dto;

import com.Minjin.TagCafe.entity.enums.CafeAttributes;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CafeDto {
    private Long cafeId;
    private Long kakaoPlaceId;
    private String cafeName;
    private double latitude;
    private double longitude;
    private String address;
    private String phoneNumber;
    private String websiteUrl;
    private LocalDateTime updateAt;
    private double averageRating;
    private String openingHours;

    private CafeAttributes.WifiSpeed wifi;
    private CafeAttributes.OutletAvailability outlets;
    private CafeAttributes.DeskSize desk;
    private CafeAttributes.RestroomAvailability restroom;
    private CafeAttributes.ParkingAvailability parking;

    private List<String> photoUrls;
    private List<String> imageUrls;

}
