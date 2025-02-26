package com.Minjin.TagCafe.entity.CafeTag;

import com.Minjin.TagCafe.entity.Cafe.Cafe;
import com.Minjin.TagCafe.entity.Tag.Tag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CafeTag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CafeTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cafeTagID;

    @ManyToOne
    @JoinColumn(name = "cafeId", nullable = false)
    @JsonIgnore
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "tagID", nullable = false)
    private Tag tag;

    @Column(nullable = false)
    private String value; // 태그 값 (예: "빠름", "일부", "없음")
}
