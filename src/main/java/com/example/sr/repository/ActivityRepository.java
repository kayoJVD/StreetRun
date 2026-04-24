package com.example.sr.repository;

import com.example.sr.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByUserId(Long userId, Pageable pageable);

    Optional<Activity> findTopByUserIdOrderByDistanceDesc(Long userId);

    @Query("SELECT a FROM Activity a WHERE a.user.id = :userId AND a.distance > 0 ORDER BY (CAST(a.duration AS double) / a.distance) ASC")
    Page<Activity> findFastestRunByUserId(Long userId, Pageable pageable);

    @Query("SELECT SUM(a.distance) FROM Activity a WHERE a.user.id = :userId")
    Double sumTotalDistanceByUserId(Long userId);

    @Query("SELECT COUNT(a.id) FROM Activity a WHERE a.user.id = :userId")
    Long countTotalActivitiesByUserId(Long userId);

    @Query("SELECT SUM(a.distance) FROM Activity a WHERE a.user.id = :userId " +
        "AND MONTH(a.date) = MONTH(CURRENT_DATE) AND YEAR(a.date) = YEAR(CURRENT_DATE)")
    Double sumMonthlyDistanceByUserId(Long userId);


    @Query("SELECT SUM(a.duration) FROM Activity a WHERE a.user.id = :userId")
    Integer sumTotalDurationByUserId(Long userId);

}
