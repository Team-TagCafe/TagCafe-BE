package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // 태그 이름으로 조회
    Optional<Tag> findByTagName(String tagName);
}
