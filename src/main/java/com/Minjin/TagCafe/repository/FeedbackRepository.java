package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
}
