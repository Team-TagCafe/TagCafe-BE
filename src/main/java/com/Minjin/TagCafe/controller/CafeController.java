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
public class CafeController {
    private final CafeService cafeService;

    // id로 카페 조회
    @GetMapping("/{id}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable("id") Long id) {
        Cafe cafe = cafeService.getCafeById(id);
        return ResponseEntity.ok(cafe);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<List<Cafe>> searchCafeByName(@RequestParam(name = "name", required = false) String name,
                                                       @RequestParam(name = "address", required = false) String address) {
        if (name != null) {
            return ResponseEntity.ok(cafeService.searchCafeByName(name));
        } else if (address != null) {
            return ResponseEntity.ok(cafeService.searchCafeByAddress(address));
        } else {
            return ResponseEntity.badRequest().build();
        }
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

