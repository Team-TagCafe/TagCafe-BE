package com.Minjin.TagCafe.service;

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

@Service
@RequiredArgsConstructor
public class SavedCafeService {
    private final SavedCafeRepository savedCafeRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    // 카페 저장
    @Transactional
    public SavedCafe saveCafe(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("카페를 찾을 수 없습니다."));

        // 중복 저장 방지
        Optional<SavedCafe> existing = savedCafeRepository.findByUserAndCafe(user, cafe);
        if (existing.isPresent()) {
            throw new IllegalStateException("이미 저장된 카페입니다.");
        }

        SavedCafe savedCafe = new SavedCafe(user, cafe);
        return savedCafeRepository.save(savedCafe);
    }

    // 저장한 카페 목록 조회
    public List<SavedCafe> getSavedCafes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return savedCafeRepository.findByUser(user);
    }

    // 저장한 카페 삭제
    @Transactional
    public void removeSavedCafe(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("카페를 찾을 수 없습니다."));
        SavedCafe savedCafe = savedCafeRepository.findByUserAndCafe(user, cafe)
                .orElseThrow(() -> new IllegalArgumentException("저장된 카페를 찾을 수 없습니다."));

        savedCafeRepository.delete(savedCafe);
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
