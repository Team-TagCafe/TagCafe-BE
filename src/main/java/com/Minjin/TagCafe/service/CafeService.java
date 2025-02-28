package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.Cafe.Cafe;
import com.Minjin.TagCafe.entity.Cafe.CafeRepository;
import com.Minjin.TagCafe.entity.CafeTag.CafeTag;
import com.Minjin.TagCafe.entity.CafeTag.CafeTagRepository;
import com.Minjin.TagCafe.entity.Tag.Tag;
import com.Minjin.TagCafe.entity.Tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Minjin.TagCafe.dto.CafeDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;
    private final CafeTagRepository cafeTagRepository;
    private final TagRepository tagRepository;

    // ID로 카페 조회
    public Cafe getCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카페를 찾을 수 없습니다."));

    }

    // 하나의 키워드로 카페 조회 (카페 이름, 주소 구분 X)
    public List<Cafe> searchCafeByKeyword(String keyword) {
        List<Cafe> nameMatches = cafeRepository.findByCafeNameContainingIgnoreCase(keyword);
        List<Cafe> addressMatches = cafeRepository.findByAddressContainingIgnoreCase(keyword);

        // 두 리스트 합치기
        return Stream.concat(nameMatches.stream(), addressMatches.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    // 특정 태그와 특정 값을 가진 카페 조회
    public List<Cafe> getCafesByTagAndValue(String tagName, String value) {
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new IllegalArgumentException("해당 태그가 존재하지 않습니다: " + tagName));
        List<CafeTag> cafeTags = cafeTagRepository.findByTagAndValue(tag, value);
        return cafeTags.stream().map(CafeTag::getCafe).collect(Collectors.toList());
    }

    // 지도 영역 내 카페 조회
    public List<Cafe> getCafesInArea(double minLat, double maxLat, double minLng, double maxLng) {
        return cafeRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);
    }

    // admin - 카페 추가
    public Cafe addCafe(CafeDto cafeDto) {
        if (cafeRepository.findByKakaoPlaceId(cafeDto.getKakaoPlaceId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 카페입니다.");
        }

        Cafe cafe = new Cafe(
                cafeDto.getKakaoPlaceId(),
                cafeDto.getCafeName(),
                cafeDto.getLatitude(),
                cafeDto.getLongitude(),
                cafeDto.getAddress(),
                cafeDto.getPhoneNumber(),
                cafeDto.getWebsiteUrl()
        );

        return cafeRepository.save(cafe);
    }
}
