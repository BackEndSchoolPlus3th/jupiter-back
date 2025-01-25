package com.jupiter.wyl.domain.movie.movie.repository;

import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
