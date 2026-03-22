package com.example.sr.repository;

import com.example.sr.domain.Activity;
import com.example.sr.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivtyRepository extends JpaRepository<Activity,Long> {
    List<Activity> findAllByUserId(Long id);
}
