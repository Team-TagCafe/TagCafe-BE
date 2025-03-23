package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.dto.SavedCafeDTO;
import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.SavedCafe;
import com.Minjin.TagCafe.entity.User;
import com.Minjin.TagCafe.repository.CafeRepository;
import com.Minjin.TagCafe.repository.SavedCafeRepository;
import com.Minjin.TagCafe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedCafeService {
    private final SavedCafeRepository savedCafeRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    // 저장한 카페 목록 조회
    public List<SavedCafeDTO> getSavedCafes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return savedCafeRepository.findByUser(user).stream()
                .map(savedCafe -> new SavedCafeDTO(savedCafe.getCafe(), savedCafe.getVisited()))
                .collect(Collectors.toList());
    }

    // 저장 여부에 따라 추가 또는 삭제 (토글 기능)
    @Transactional
    public boolean toggleSavedStatus(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("카페를 찾을 수 없습니다."));

        Optional<SavedCafe> existing = savedCafeRepository.findByUserAndCafe(user, cafe);

        if (existing.isPresent()) {
            // 이미 저장된 경우 → 삭제
            savedCafeRepository.delete(existing.get());
            return false; // 삭제됨
        } else {
            // 저장되지 않은 경우 → 새로 추가
            SavedCafe savedCafe = new SavedCafe(user, cafe);
            savedCafeRepository.save(savedCafe);
            return true; // 저장됨
        }
    }

    // 방문 여부 업데이트
    @Transactional
    public SavedCafe toggleVisitedStatus(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("카페를 찾을 수 없습니다."));

        SavedCafe savedCafe = savedCafeRepository.findByUserAndCafe(user, cafe)
                .orElseThrow(() -> new IllegalArgumentException("저장된 카페를 찾을 수 없습니다."));

        // 방문 여부를 토글 (true <-> false)
        savedCafe.setVisited(!savedCafe.getVisited());
        return savedCafeRepository.save(savedCafe);
    }

    public List<SavedCafeDTO> getSavedCafesWithFilter(Long userId, List<String> tagNames, List<String> values) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Cafe> savedCafes = savedCafeRepository.findByUser(user)
                .stream()
                .map(SavedCafe::getCafe)
                .collect(Collectors.toList());

        // 🔹 tagNames와 values가 null이면 빈 리스트로 초기화
        if (tagNames == null) tagNames = new ArrayList<>();
        if (values == null) values = new ArrayList<>();

        // 평점 필터 적용 (존재하는 경우)
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

            tagNames.remove(index);
            values.remove(index);

            savedCafes = savedCafes.stream()
                    .filter(cafe -> cafe.getAverageRating() >= minGrade)
                    .collect(Collectors.toList());
        }

        // 태그 필터 적용 (Cafe 엔티티 필드 기반)
        for (int i = 0; i < tagNames.size(); i++) {
            String tagName = tagNames.get(i);
            String value = values.get(i);

            final String filterValue = value;
            savedCafes = savedCafes.stream()
                    .filter(cafe -> cafeMatchesTag(cafe, tagName, filterValue))
                    .collect(Collectors.toList());
        }

        return savedCafes.stream()
                .map(cafe -> new SavedCafeDTO(cafe, findSavedCafe(user, cafe).getVisited()))
                .collect(Collectors.toList());
    }

    private boolean cafeMatchesTag(Cafe cafe, String tagName, String value) {
        return switch (tagName) {
            case "와이파이" -> cafe.getWifi() != null && cafe.getWifi().name().equals(value);
            case "콘센트" -> cafe.getOutlets() != null && cafe.getOutlets().name().equals(value);
            case "책상" -> cafe.getDesk() != null && cafe.getDesk().name().equals(value);
            case "화장실" -> cafe.getRestroom() != null && cafe.getRestroom().name().equals(value);
            case "주차" -> cafe.getParking() != null && cafe.getParking().name().equals(value);
            default -> false;
        };
    }

    private SavedCafe findSavedCafe(User user, Cafe cafe) {
        return savedCafeRepository.findByUserAndCafe(user, cafe)
                .orElseThrow(() -> new IllegalArgumentException("저장된 카페를 찾을 수 없습니다."));
    }

}
