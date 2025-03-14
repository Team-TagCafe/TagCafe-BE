package com.Minjin.TagCafe.entity;

import com.Minjin.TagCafe.entity.enums.CafeAttributes.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe; // 연관된 카페

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WifiSpeed wifi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutletAvailability outlets;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeskSize desk;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length=50)
    private RestroomAvailability restroom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingAvailability parking;
}