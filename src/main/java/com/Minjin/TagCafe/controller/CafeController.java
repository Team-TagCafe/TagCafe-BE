package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.CafeDto;
import com.Minjin.TagCafe.dto.CafeHomeDTO;
import com.Minjin.TagCafe.dto.CafeSearchDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cafes")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000") // React에서 API 호출 허용
public class CafeController {
    private final CafeService cafeService;
    private final CafeRepository cafeRepository;

    // id로 카페 조회
    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeDto> getCafeById(@PathVariable("cafeId") Long cafeId) {
        Cafe cafe = cafeService.getCafeById(cafeId);
        List<String> base64Images = cafe.getImages().stream()
                .map(image -> Base64.getEncoder().encodeToString(image.getImageData()))
                .collect(Collectors.toList());
        CafeDto cafeDto = new CafeDto(
                cafe.getCafeId(),
                cafe.getKakaoPlaceId(),
                cafe.getCafeName(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getAddress(),
                cafe.getPhoneNumber(),
                cafe.getWebsiteUrl(),
                cafe.getUpdateAt(),
                cafe.getAverageRating(),
                cafe.getOpeningHours(),
                cafe.getWifi(),
                cafe.getOutlets(),
                cafe.getDesk(),
                cafe.getRestroom(),
                cafe.getParking(),
                null,
                base64Images
        );
        return ResponseEntity.ok(cafeDto);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<List<CafeSearchDTO>> searchCafe(@RequestParam(name = "query") String query) {
        List<Cafe> cafes = cafeService.searchCafeByKeyword(query);

        List<CafeSearchDTO> dtos = cafes.stream()
                .map(cafe -> new CafeSearchDTO(cafe.getCafeId(), cafe.getCafeName(), cafe.getAddress()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // 지도 영역 내 카페 조회 (위경도 범위 내 검색)
    @GetMapping("/area")
    public ResponseEntity<List<CafeHomeDTO>> getCafesInArea(@RequestParam(name = "minLat") double minLat,
                                                            @RequestParam(name = "maxLat") double maxLat,
                                                            @RequestParam(name = "minLng") double minLng,
                                                            @RequestParam(name = "maxLng") double maxLng) {
        List<Cafe> cafes = cafeService.getCafesInArea(minLat, maxLat, minLng, maxLng);

        List<CafeHomeDTO> dtos = cafes.stream()
                .map(cafe -> {
                    String imageBase64 = cafe.getImages().isEmpty() ? null
                            : Base64.getEncoder().encodeToString(cafe.getImages().get(0).getImageData());
                    return new CafeHomeDTO(
                            cafe.getCafeId(),
                            cafe.getCafeName(),
                            cafe.getLatitude(),
                            cafe.getLongitude(),
                            cafe.getAddress(),
                            cafe.getAverageRating(),
                            cafe.getOpeningHours(),
                            cafe.getWifi(),
                            cafe.getOutlets(),
                            cafe.getDesk(),
                            cafe.getRestroom(),
                            cafe.getParking(),
                            imageBase64
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // 특정 태그와 특정 값을 가진 카페 조회
    @GetMapping("/filter")
    public ResponseEntity<List<CafeHomeDTO>> getCafesByMultipleTags(@RequestParam(name = "tagNames") List<String> tagNames,
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

        List<CafeHomeDTO> dtos = cafes.stream().map(cafe -> {
            String imageBase64 = cafe.getImages() != null && !cafe.getImages().isEmpty()
                    ? Base64.getEncoder().encodeToString(cafe.getImages().get(0).getImageData())
                    : null;

            return new CafeHomeDTO(
                    cafe.getCafeId(),
                    cafe.getCafeName(),
                    cafe.getLatitude(),
                    cafe.getLongitude(),
                    cafe.getAddress(),
                    cafe.getAverageRating(),
                    cafe.getOpeningHours(),
                    cafe.getWifi(),
                    cafe.getOutlets(),
                    cafe.getDesk(),
                    cafe.getRestroom(),
                    cafe.getParking(),
                    imageBase64
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    // admin - 카페 검색 후 db 저장
    @PostMapping
    public ResponseEntity<?> addCafe(@RequestBody CafeDto cafeDto) {
        Cafe savedCafe = cafeService.addCafe(cafeDto);
        return ResponseEntity.ok(savedCafe);
    }

    // admin - 태그 값 업데이트
    @PutMapping("/{cafeId}/tags")
    public ResponseEntity<Cafe> updateCafeTags(@PathVariable("cafeId") Long cafeId,
                                               @RequestBody CafeDto cafeDto) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카페를 찾을 수 없습니다."));

        if (cafeDto.getWifi() != null) cafe.setWifi(cafeDto.getWifi());
        if (cafeDto.getOutlets() != null) cafe.setOutlets(cafeDto.getOutlets());
        if (cafeDto.getDesk() != null) cafe.setDesk(cafeDto.getDesk());
        if (cafeDto.getRestroom() != null) cafe.setRestroom(cafeDto.getRestroom());
        if (cafeDto.getParking() != null) cafe.setParking(cafeDto.getParking());

        Cafe updatedCafe = cafeRepository.save(cafe);
        return ResponseEntity.ok(updatedCafe);
    }

    // 모든 카페 조회 API 추가
    @GetMapping
    public ResponseEntity<List<Cafe>> getAllCafes() {
        List<Cafe> cafes = cafeService.getAllCafes();
        return ResponseEntity.ok(cafes);
    }

    @GetMapping("/{cafeId}/tags")
    public ResponseEntity<Map<String, String>> getCafeTags(@PathVariable("cafeId") Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카페를 찾을 수 없습니다."));

        Map<String, String> tags = new HashMap<>();

        // null 값 방지 (기본 값 설정)
        tags.put("wifi", getEnumNameOrDefault(cafe.getWifi()));
        tags.put("outlets", getEnumNameOrDefault(cafe.getOutlets()));
        tags.put("desk", getEnumNameOrDefault(cafe.getDesk()));
        tags.put("restroom", getEnumNameOrDefault(cafe.getRestroom()));
        tags.put("parking", getEnumNameOrDefault(cafe.getParking()));

        return ResponseEntity.ok(tags);
    }

    // Enum 값이 null이면 "-"을 반환
    private String getEnumNameOrDefault(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name() : "-";
    }

}

