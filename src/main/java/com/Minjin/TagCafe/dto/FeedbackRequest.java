package com.Minjin.TagCafe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    private String content;
    private String email;
}