package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.entity.SavedCafe;
import com.Minjin.TagCafe.service.SavedCafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-cafes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SavedCafeController {
    private final SavedCafeService savedCafeService;

    // 카페 저장
    @PostMapping("/{cafeId}")
    public ResponseEntity<SavedCafe> saveCafe(
            @RequestParam("userId") Long userId,
            @PathVariable("cafeId") Long cafeId) {
        return ResponseEntity.ok(savedCafeService.saveCafe(userId, cafeId));
    }

    // 저장한 카페 목록 조회
    @GetMapping
    public ResponseEntity<List<SavedCafe>> getSavedCafes(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(savedCafeService.getSavedCafes(userId));
    }

    // 저장한 카페 삭제
    @DeleteMapping("/{cafeId}")
    public ResponseEntity<Void> removeSavedCafe(@RequestParam("userId") Long userId, @PathVariable("cafeId") Long cafeId) {
        savedCafeService.removeSavedCafe(userId, cafeId);
        return ResponseEntity.noContent().build();
    }

    // 방문여부 업데이트
    @PatchMapping("/{cafeId}/visited")
    public ResponseEntity<SavedCafe> toggleVisited(
            @RequestParam("userId") Long userId,
            @PathVariable("cafeId") Long cafeId) {

        return ResponseEntity.ok(savedCafeService.toggleVisitedStatus(userId, cafeId));
    }
}
