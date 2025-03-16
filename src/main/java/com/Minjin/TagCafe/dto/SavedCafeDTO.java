package com.Minjin.TagCafe.dto;

import com.Minjin.TagCafe.entity.Cafe;
import lombok.Getter;

@Getter
public class SavedCafeDTO {
    private Long cafeId;
    private String cafeName;
    private String address;
    private Boolean visited;

    public SavedCafeDTO(Cafe cafe, Boolean visited) {
        this.cafeId = cafe.getCafeId();
        this.cafeName = cafe.getCafeName();
        this.address = cafe.getAddress();
        this.visited = visited;
    }
}
