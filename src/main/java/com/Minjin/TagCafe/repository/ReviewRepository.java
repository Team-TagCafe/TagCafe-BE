package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCafe_CafeId(Long cafeId);
}