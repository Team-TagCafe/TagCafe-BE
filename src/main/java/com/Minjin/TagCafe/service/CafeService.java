package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.Cafe.Cafe;
import com.Minjin.TagCafe.entity.Cafe.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;

    // ID로 카페 조회
    public Cafe getCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카페를 찾을 수 없습니다."));

    }

    // 카페 이름으로 카페 조회
    public List<Cafe> searchCafeByName(String name) {
        return cafeRepository.findByCafeNameContainingIgnoreCase(name);
    }

    // 주소로 카페 조회
    public List<Cafe> searchCafeByAddress(String address) {
        return cafeRepository.findByAddressContainingIgnoreCase(address);
    }

    // 지도 영역 내 카페 조회
    public List<Cafe> getCafesInArea(double minLat, double maxLat, double minLng, double maxLng) {
        return cafeRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);
    }
}
