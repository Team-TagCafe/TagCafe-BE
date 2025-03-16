package com.Minjin.TagCafe.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CafeAttributes {
    public enum WifiSpeed {
        빠름, 보통, 없음
    }

    public enum OutletAvailability {
        자리마다, 일부, 없음
    }

    public enum DeskSize {
        넓음, 적당함, 좁음
    }

    public enum RestroomAvailability {
        외부, 실내
    }

    public enum ParkingAvailability {
        @JsonProperty("가능_무료") 가능_무료,
        @JsonProperty("가능_유료") 가능_유료,
        @JsonProperty("불가능") 불가능,
        @JsonProperty("가능_일부") 가능_일부;
    }
}