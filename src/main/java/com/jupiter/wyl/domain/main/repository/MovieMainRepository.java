package com.jupiter.wyl.domain.main.repository;

import com.jupiter.wyl.domain.main.entity.MovieMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieMainRepository extends JpaRepository<MovieMain, Long> {
    List<MovieMain> findByCategory(String category);
}
