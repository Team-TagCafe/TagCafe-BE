package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.CafeTag;
import com.Minjin.TagCafe.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeTagRepository extends JpaRepository<CafeTag, Long> {
    // 특정 카페의 태그 조회
    List<CafeTag> findByCafe(Cafe cafe);

    // 특정 태그 & 특정 값을 가진 카페 조회
    List<CafeTag> findByTagAndValue(Tag tag, String value);
}
