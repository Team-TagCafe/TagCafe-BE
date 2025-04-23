package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.SavedCafeDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.SavedCafe;
import com.Minjin.TagCafe.service.SavedCafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SavedCafe", description = "사용자 저장한 카페 및 방문여부 관리 API")
@RestController
@RequestMapping("/saved-cafes")
@RequiredArgsConstructor
public class SavedCafeController {
    private final SavedCafeService savedCafeService;

    // 저장한 카페 목록 조회
    @Operation(summary = "저장한 카페 목록 조회", description = "사용자의 userId를 기준으로 저장한 카페 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SavedCafeDTO>> getSavedCafes(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(savedCafeService.getSavedCafes(userId));
    }

    // 카페 저장 여부 토글 (저장 or 삭제)
    @Operation(summary = "카페 저장 토글", description = "카페를 저장하거나 삭제(찜 해제)합니다. 이미 저장되어 있으면 삭제됩니다.")
    @PatchMapping("/{cafeId}")
    public ResponseEntity<Boolean> toggleSavedStatus(
            @RequestParam("userId") Long userId,
            @PathVariable("cafeId") Long cafeId) {

        boolean isSaved = savedCafeService.toggleSavedStatus(userId, cafeId);
        return ResponseEntity.ok(isSaved); // 저장 여부 반환
    }

    // 방문여부 업데이트
    @Operation(summary = "카페 방문여부 토글", description = "카페의 방문 여부(방문함/안함)를 변경합니다.")
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
    @Operation(summary = "저장한 카페 필터링 조회", description = "저장한 카페 목록을 태그 조건에 따라 필터링해서 조회합니다.")
    @GetMapping("/filter")
    public ResponseEntity<List<SavedCafeDTO>> getSavedCafesWithFilter(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "tags", required = false) List<String> tagNames,
            @RequestParam(value = "values", required = false) List<String> values) {

        return ResponseEntity.ok(savedCafeService.getSavedCafesWithFilter(userId, tagNames, values));
    }

}
