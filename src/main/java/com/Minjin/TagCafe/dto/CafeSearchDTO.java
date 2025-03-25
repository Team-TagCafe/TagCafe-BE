package com.Minjin.TagCafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CafeSearchDTO {
    private Long cafeId;
    private String cafeName;
    private String address;

}
