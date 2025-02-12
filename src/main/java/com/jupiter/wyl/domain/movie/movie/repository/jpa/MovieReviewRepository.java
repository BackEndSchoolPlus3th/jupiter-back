package com.jupiter.wyl.domain.movie.movie.repository.jpa;

import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {
    List<MovieReview> findAllByMovieId(Long movieId);
    Optional<MovieReview> findByUserIdAndMovieId(Long userId, Long movieId);
}
