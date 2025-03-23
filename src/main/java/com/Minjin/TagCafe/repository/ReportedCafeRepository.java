package com.Minjin.TagCafe.repository;

import com.Minjin.TagCafe.entity.ReportedCafe;
import com.Minjin.TagCafe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportedCafeRepository extends JpaRepository<ReportedCafe, Long> {
    List<ReportedCafe> findByUserEmail(String userEmail);

    List<ReportedCafe> findByApprovedFalse();


}
