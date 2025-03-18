package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.SavedCafe;
import com.Minjin.TagCafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedCafeRepository extends JpaRepository<SavedCafe, Long> {
    List<SavedCafe> findByUser(User user);
    Optional<SavedCafe> findByUserAndCafe(User user, Cafe cafe);
}
