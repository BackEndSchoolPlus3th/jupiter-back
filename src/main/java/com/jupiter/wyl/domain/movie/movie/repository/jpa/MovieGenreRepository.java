package com.jupiter.wyl.domain.movie.movie.repository.jpa;

import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenre,Long> {
}
