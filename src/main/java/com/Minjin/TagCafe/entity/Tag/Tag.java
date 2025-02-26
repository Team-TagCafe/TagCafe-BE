package com.Minjin.TagCafe.entity.Tag;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tagID;

    @Column(nullable = false, unique = true) // unique로 중복 태그 방지
    private String tagName;
}
