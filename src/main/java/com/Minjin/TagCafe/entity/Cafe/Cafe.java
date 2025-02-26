package com.Minjin.TagCafe.entity.Cafe;

import com.Minjin.TagCafe.entity.CafeTag.CafeTag;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private double averageGrade = 0.0; // 기본값 0.0

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CafeTag> tags;
}
