package com.Minjin.TagCafe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ReviewDTO {
    private Long cafeId;
    private String userEmail;
    private int rating;
    private String content;
    private Set<Long> tagIds; // 태그 ID 목록
}