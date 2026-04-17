package com.example.sr.repository;

import com.example.sr.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Page<Activity> findAllByUserId(Long userId, Pageable pageable);

}
