package com.Minjin.TagCafe.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CafeDto {
    private Long kakaoPlaceId;
    private String cafeName;
    private double latitude;
    private double longitude;
    private String address;
    private String phoneNumber;
    private String websiteUrl;
}
