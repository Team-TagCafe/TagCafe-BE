package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.entity.Cafe.Cafe;
import com.Minjin.TagCafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cafes")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000") // React에서 API 호출 허용
public class CafeController {
    private final CafeService cafeService;

    // id로 카페 조회
    @GetMapping("/{cafeId}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable("cafeId") Long cafeId) {
        Cafe cafe = cafeService.getCafeById(cafeId);
        return ResponseEntity.ok(cafe);
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
}

