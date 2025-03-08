package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.QA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QARepository extends JpaRepository<QA, Long> {
}