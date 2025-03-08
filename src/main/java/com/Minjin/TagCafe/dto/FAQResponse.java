package com.Minjin.TagCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FAQResponse {
    private String category;
    private String question;
    private String answer;
}