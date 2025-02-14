package com.Minjin.TagCafe.entity.Cafe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe,Long> {
    // 카카오맵 API 연동
    List<Cafe> findByKakaoPlaceId(Long kakaoPlaceId);

    // 카페 이름 검색
    List<Cafe> findByCafeNameContainingIgnoreCase(String cafeName);

    // 주소 검색
    List<Cafe> findByAddressContainingIgnoreCase(String address);
}
