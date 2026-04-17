package com.example.sr.repository;

import com.example.sr.domain.Sports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SportsRepository extends JpaRepository<Sports, Long> {

}
