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

}
