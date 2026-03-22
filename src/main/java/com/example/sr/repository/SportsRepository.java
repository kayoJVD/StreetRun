package com.example.sr.repository;

import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SportsRepository extends JpaRepository<Sports, Long> {

}
