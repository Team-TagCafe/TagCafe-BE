package com.Minjin.TagCafe.entity;

import com.Minjin.TagCafe.entity.enums.CafeAttributes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reported_cafe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportedCafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reported_cafe_id")
    private Long reportedCafeId;

    @Column(nullable = false)
    private String userEmail;

    private String cafeName;

    private String address;

    private String kakaoPlaceId;

    private Double latitude;

    private Double longitude;

    private String phoneNumber;

    private String openingHours;

    private String websiteUrl;

    @Enumerated(EnumType.STRING)
    private CafeAttributes.WifiSpeed wifi;

    @Enumerated(EnumType.STRING)
    private CafeAttributes.OutletAvailability outlets;

    @Enumerated(EnumType.STRING)
    private CafeAttributes.DeskSize desk;

    @Enumerated(EnumType.STRING)
    private CafeAttributes.RestroomAvailability restroom;

    @Enumerated(EnumType.STRING)
    private CafeAttributes.ParkingAvailability parking;

    @Column(length = 200)
    private String content;

    private LocalDateTime createdAt;

    private boolean approved = false;
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

}
