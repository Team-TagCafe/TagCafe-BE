package com.Minjin.TagCafe.entity.CafeTag;

import com.Minjin.TagCafe.entity.Cafe.Cafe;
import com.Minjin.TagCafe.entity.Tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeTagRepository extends JpaRepository<CafeTag, Integer> {
    // 특정 카페의 태그 조회
    List<CafeTag> findByCafe(Cafe cafe);

    // 특정 태그를 가진 모든 카페 조회
    List<CafeTag> findByTag(Tag tag);
}
