package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.entity.CafeTag;
import com.Minjin.TagCafe.repository.CafeTagRepository;
import com.Minjin.TagCafe.entity.Tag;
import com.Minjin.TagCafe.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Minjin.TagCafe.dto.CafeDto;

import java.util.List;
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

    // 특정 태그와 값 리스트를 가진 카페 조회 (다중 필터링 지원)
    public List<Cafe> getCafesByMultipleTagsAndValues(List<String> tagNames, List<String> values) {
        List<Cafe> filteredCafes = cafeRepository.findAll(); // 기본적으로 모든 카페 가져오기

        // 평점 필터 확인 및 처리
        boolean hasGradeFilter = tagNames.contains("평점");
        if (hasGradeFilter) {
            int index = tagNames.indexOf("평점");
            String gradeFilter = values.get(index);

            final double minGrade = switch (gradeFilter) {
                case "5.0" -> 5.0;
                case "4.0 이상" -> 4.0;
                case "3.0 이상" -> 3.0;
                default -> 0.0;
            };

            // 평점 태그를 리스트에서 제거 (CafeTag 검색에서 제외)
            tagNames.remove(index);
            values.remove(index);

            // 평균 평점이 기준 이상인 카페 필터링
            filteredCafes = filteredCafes.stream()
                    .filter(cafe -> cafe.getAverageGrade() >= minGrade)
                    .collect(Collectors.toList());
        }

        // 태그 기반 필터링 (평점 외 나머지 태그 적용)
        for (int i = 0; i < tagNames.size(); i++) {
            String tagName = tagNames.get(i);
            String value = values.get(i);

            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new IllegalArgumentException("태그를 찾을 수 없습니다: " + tagName));

            List<CafeTag> cafeTags = cafeTagRepository.findByTagAndValue(tag, value);
            List<Cafe> filteredByTag = cafeTags.stream().map(CafeTag::getCafe).collect(Collectors.toList());

            filteredCafes.retainAll(filteredByTag);
        }

        return filteredCafes;
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
