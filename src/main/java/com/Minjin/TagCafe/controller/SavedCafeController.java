package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.SavedCafeDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.SavedCafe;
import com.Minjin.TagCafe.service.SavedCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-cafes")
@RequiredArgsConstructor
public class SavedCafeController {
    private final SavedCafeService savedCafeService;

    // 저장한 카페 목록 조회
    @GetMapping
    public ResponseEntity<List<SavedCafeDTO>> getSavedCafes(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(savedCafeService.getSavedCafes(userId));
    }

    // 카페 저장 여부 토글 (저장 or 삭제)
    @PatchMapping("/{cafeId}")
    public ResponseEntity<Boolean> toggleSavedStatus(
            @RequestParam("userId") Long userId,
            @PathVariable("cafeId") Long cafeId) {

        boolean isSaved = savedCafeService.toggleSavedStatus(userId, cafeId);
        return ResponseEntity.ok(isSaved); // 저장 여부 반환
    }

    // 방문여부 업데이트
    @PatchMapping("/{cafeId}/visited")
    public ResponseEntity<SavedCafeDTO> toggleVisited(
            @RequestParam("userId") Long userId,
            @PathVariable("cafeId") Long cafeId) {

        SavedCafe savedCafe = savedCafeService.toggleVisitedStatus(userId, cafeId);
        Cafe cafe = savedCafe.getCafe();
        Boolean visited = savedCafe.getVisited();

        return ResponseEntity.ok(new SavedCafeDTO(cafe, visited));
    }

    // 저장한 카페 목록 + 필터링 적용
    @GetMapping("/filter")
    public ResponseEntity<List<SavedCafeDTO>> getSavedCafesWithFilter(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "tags", required = false) List<String> tagNames,
            @RequestParam(value = "values", required = false) List<String> values) {

        return ResponseEntity.ok(savedCafeService.getSavedCafesWithFilter(userId, tagNames, values));
    }

}
