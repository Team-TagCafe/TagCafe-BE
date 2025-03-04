package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CafeRepository extends JpaRepository<Cafe,Long> {
    // 카카오맵 API 연동
    Optional<Cafe> findByKakaoPlaceId(Long kakaoPlaceId);

    // 카페 이름 검색
    List<Cafe> findByCafeNameContainingIgnoreCase(String cafeName);

    // 주소 검색
    List<Cafe> findByAddressContainingIgnoreCase(String address);

    // 지도 영역 내 카페 조회 (위경도 범위 내 검색)
    List<Cafe> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng, double maxLng);
}
