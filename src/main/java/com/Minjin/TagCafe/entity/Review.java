package com.Minjin.TagCafe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cafeId; // 카페 ID
    private String userEmail; // 리뷰 작성자 이메일
    private int rating; // 별점
    private String content; // 리뷰 내용

    @ManyToMany
    @JoinTable(
            name = "review_tags",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>(); // 리뷰에 연결된 태그 목록

    @Column(nullable = false, updatable = false)
    private String createdAt; // 작성 날짜

    public Review(Long cafeId, String userEmail, int rating, String content, Set<Tag> tags, String createdAt) {
        this.cafeId = cafeId;
        this.userEmail = userEmail;
        this.rating = rating;
        this.content = content;
        this.tags = tags;
        this.createdAt = createdAt;
    }
}