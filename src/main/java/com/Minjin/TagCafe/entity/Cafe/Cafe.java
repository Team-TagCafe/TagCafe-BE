package com.Minjin.TagCafe.entity.Cafe;

import com.Minjin.TagCafe.entity.CafeTag.CafeTag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cafe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeId;

    @Column(nullable = false)
    private Long kakaoPlaceId;

    @Column(nullable = false)
    private String cafeName;

    private Float averageGrade;

    @Column(nullable = false)
    private String address;

    @Column
    private String operatingHour;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeTag> cafeTags= new ArrayList<>();
}
