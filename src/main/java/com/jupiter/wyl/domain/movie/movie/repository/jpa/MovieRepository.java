package com.jupiter.wyl.domain.movie.movie.repository.jpa;

import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByTitle(String title);
}
