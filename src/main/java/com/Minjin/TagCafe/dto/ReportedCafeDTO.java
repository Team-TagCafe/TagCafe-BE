package com.Minjin.TagCafe.dto;

import com.Minjin.TagCafe.entity.ReportedCafe;
import com.Minjin.TagCafe.entity.enums.CafeAttributes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportedCafeDTO {
    private Long reportedCafeId;
    private String userEmail;
    private String cafeName;
    private String address;
    private String kakaoPlaceId;
    private CafeAttributes.WifiSpeed wifi;
    private CafeAttributes.OutletAvailability outlets;
    private CafeAttributes.DeskSize desk;
    private CafeAttributes.RestroomAvailability restroom;
    private CafeAttributes.ParkingAvailability parking;
    private String content;
    private String status;
    private Long cafeId;
    private int rating;


    public static ReportedCafeDTO fromEntity(ReportedCafe report) {
        return ReportedCafeDTO.builder()
                .reportedCafeId(report.getReportedCafeId())
                .userEmail(report.getUserEmail())
                .cafeName(report.getCafeName())
                .address(report.getAddress())
                .kakaoPlaceId(report.getKakaoPlaceId())
                .wifi(report.getWifi())
                .outlets(report.getOutlets())
                .desk(report.getDesk())
                .restroom(report.getRestroom())
                .parking(report.getParking())
                .content(report.getContent())
                .status(report.getStatus().name())
                .cafeId(report.getCafe() != null ? report.getCafe().getCafeId() : null)
                .rating(report.getRating())
                .build();
    }

}


