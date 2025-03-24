package com.Minjin.TagCafe.entity;

import com.Minjin.TagCafe.entity.enums.CafeAttributes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Cafe",
        uniqueConstraints = {@UniqueConstraint(columnNames = "kakaoPlaceId")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeId;

    @Column(nullable = false, unique = true)
    private Long kakaoPlaceId;

    @Column(nullable = false)
    private String cafeName;

    @Column(nullable = false)
    private double latitude;   // 위도

    @Column(nullable = false)
    private double longitude;  // 경도

    @Column(nullable = false)
    private String address;

    private String openingHours;
    private String phoneNumber;
    private String websiteUrl;

    @Column(length = 1000)
    private String photoUrl;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private double averageRating = 0.0; // 기본값 0.0

    @Enumerated(EnumType.STRING)
    @Column
    private CafeAttributes.WifiSpeed wifi;

    @Enumerated(EnumType.STRING)
    @Column
    private CafeAttributes.OutletAvailability outlets;

    @Enumerated(EnumType.STRING)
    @Column
    private CafeAttributes.DeskSize desk;

    @Enumerated(EnumType.STRING)
    @Column
    private CafeAttributes.RestroomAvailability restroom;

    @Enumerated(EnumType.STRING)
    @Column
    private CafeAttributes.ParkingAvailability parking;


    public Cafe(Long kakaoPlaceId, String cafeName, double latitude, double longitude, String address, String phoneNumber, String websiteUrl) {
        this.kakaoPlaceId = kakaoPlaceId;
        this.cafeName = cafeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUrl = websiteUrl;
        this.updateAt = LocalDateTime.now(); // 자동으로 현재 시간 설정
        this.averageRating = 0.0; // 기본값 설정
    }
}
