package com.jupiter.wyl.domain.movie.movie.service;

import com.jupiter.wyl.domain.member.service.MemberService;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieReviewDto;
import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
import com.jupiter.wyl.domain.movie.movie.repository.jpa.MovieRepository;
import com.jupiter.wyl.domain.movie.movie.repository.jpa.MovieReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieReviewService {

    @Autowired
    private final MovieReviewRepository movieReviewRepository;
    private final MovieRepository movieRepository;
    private final MemberService memberService;

    public void saveReview(Long userId, String reviewContent, int rating, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다."));

        MovieReview movieReview = new MovieReview();

        movieReview.setUserId(userId);
        movieReview.setMovie(movie);
        movieReview.setReviewContent(reviewContent);
        movieReview.setRating(rating);

        movieReviewRepository.save(movieReview);
    }

//    public List<MovieReviewDto> findAllByMovieId(Long movieId) {
//        List<MovieReview> movieReviews = movieReviewRepository.findAllByMovieId(movieId);
//
//        if (movieReviews.isEmpty()) {
//            throw new RuntimeException("해당 영화에 대한 리뷰가 없습니다: " + movieId);
//        }
//
//        return movieReviews.stream()
//                .map(review -> MovieReviewDto.builder()
//                        .id(review.getId())
//                        .reviewContent(review.getReviewContent())
//                        .rating(review.getRating())
//                        .movie(review.getMovie().getId())
//                        .userId(review.getUserId())
//                        .build())
//                .collect(Collectors.toList());
//    }

    public List<MovieReviewDto> findAllByMovieId(Long movieId) {
        List<MovieReview> movieReviews = movieReviewRepository.findAllByMovieId(movieId);

        return movieReviews.stream().map(review -> {
            String userName = memberService.getUserNameById(review.getUserId()); // ✅ userId로 userName 조회
            return MovieReviewDto.fromEntity(review, userName); // ✅ DTO에 userName 추가
        }).collect(Collectors.toList());
    }

    public MovieReviewDto getReviewByUserAndMovie(Long userId, Long movieId) {
        MovieReview movieReview = movieReviewRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        String userName = memberService.getUserNameById(userId);

        return MovieReviewDto.fromEntity(movieReview, userName); // ✅ DTO로 변환하여 반환
    }

    public void updateReview(Long reviewId, String newContent, int newRating) {
        MovieReview movieReview = movieReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다."));

        movieReview.setReviewContent(newContent);
        movieReview.setRating(newRating);

        movieReviewRepository.save(movieReview);
    }
}
