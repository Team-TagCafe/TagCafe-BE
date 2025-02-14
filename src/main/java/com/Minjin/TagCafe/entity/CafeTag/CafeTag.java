package com.Minjin.TagCafe.entity.CafeTag;

import com.Minjin.TagCafe.entity.Cafe.Cafe;
import com.Minjin.TagCafe.entity.Tag.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CafeTag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CafeTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cafeTagID;

    @ManyToOne
    @JoinColumn(name = "cafeID", nullable = false)
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "tagID", nullable = false)
    private Tag tag;

    @Column(nullable = false)
    private String value;
}
