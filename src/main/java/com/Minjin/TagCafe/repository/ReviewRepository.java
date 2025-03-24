package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCafe_CafeId(Long cafeId);
    List<Review> findByUserEmail(String userEmail);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.cafe = :cafe")
    Double findAverageRatingByCafe(@Param("cafe") Cafe cafe);
}