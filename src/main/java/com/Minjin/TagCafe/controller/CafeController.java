package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.CafeDto;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cafes")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000") // React에서 API 호출 허용
public class CafeController {
    private final CafeService cafeService;

    // id로 카페 조회
    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeDto> getCafeById(@PathVariable("cafeId") Long cafeId) {
        Cafe cafe = cafeService.getCafeById(cafeId);
        CafeDto cafeDto = new CafeDto(
                cafe.getKakaoPlaceId(),
                cafe.getCafeName(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getAddress(),
                cafe.getPhoneNumber(),
                cafe.getWebsiteUrl()
        );
        return ResponseEntity.ok(cafeDto);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<List<Cafe>> searchCafe(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(cafeService.searchCafeByKeyword(query));
    }

    // 지도 영역 내 카페 조회 (위경도 범위 내 검색)
    @GetMapping("/area")
    public ResponseEntity<List<Cafe>> getCafesInArea(@RequestParam(name = "minLat") double minLat,
                                                     @RequestParam(name = "maxLat") double maxLat,
                                                     @RequestParam(name = "minLng") double minLng,
                                                     @RequestParam(name = "maxLng") double maxLng) {
        List<Cafe> cafes = cafeService.getCafesInArea(minLat, maxLat, minLng, maxLng);
        return ResponseEntity.ok(cafes);
    }

    // 특정 태그와 특정 값을 가진 카페 조회
    @GetMapping("/filter")
    public ResponseEntity<List<Cafe>> getCafesByMultipleTags(@RequestParam(name = "tagNames") List<String> tagNames,
                                                             @RequestParam(name = "values") List<String> values) {
        if (tagNames.size() != values.size()) {
            return ResponseEntity.badRequest().build();
        }

        // null 값 또는 빈 문자열이 포함된 필터 제거
        List<String> validTagNames = new ArrayList<>();
        List<String> validValues = new ArrayList<>();

        for (int i = 0; i < tagNames.size(); i++) {
            if (values.get(i) != null && !values.get(i).isEmpty()) {
                validTagNames.add(tagNames.get(i));
                validValues.add(values.get(i));
            }
        }

        List<Cafe> cafes = cafeService.getCafesByMultipleTagsAndValues(tagNames, values);
        return ResponseEntity.ok(cafes);
    }


    // admin - 카페 검색 후 db 저장
    @PostMapping
    public ResponseEntity<?> addCafe(@RequestBody CafeDto cafeDto) {
        Cafe savedCafe = cafeService.addCafe(cafeDto);
        return ResponseEntity.ok(savedCafe);
    }

}

