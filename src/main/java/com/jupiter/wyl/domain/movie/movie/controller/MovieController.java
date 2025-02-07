package com.jupiter.wyl.domain.movie.movie.controller;


import com.jupiter.wyl.domain.movie.movie.dto.request.ReviewRequest;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieReviewDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieSearchDto;
import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
import com.jupiter.wyl.domain.movie.movie.service.MovieSearchService;
import com.jupiter.wyl.domain.movie.movie.service.MovieService;
import com.jupiter.wyl.domain.movie.movie.service.MovieReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/movie")
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieReviewService movieReviewService;
    private final MovieSearchService movieSearchService;

    @GetMapping
    public List<MovieDto> findAllMovies(){
        List<MovieDto> movieDtos = movieService.findAll();
        return movieDtos;
    }

    @GetMapping("/search")
    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirector(@RequestParam("word") String word){
        List<MovieSearchDto> movieSearchDtos = movieSearchService.findByTitleOrOverviewOrActorsOrDirector(word);
        return movieSearchDtos;
    }

    @GetMapping("/search/popular")
    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirectorPopular(@RequestParam("word") String word){
        List<MovieSearchDto> movieSearchDtos = movieSearchService.findByTitleOrOverviewOrActorsOrDirectorPopular(word);
        return movieSearchDtos;
    }

    @GetMapping("/{id}")
    public MovieDto getMovie(@PathVariable("id") Long id) {
        MovieDto movie = movieService.findById(id);
        return movie;
    }

    @GetMapping("/review/{movieId}")
    public List<MovieReviewDto> getMovieReview(@PathVariable("movieId") Long movieId) {
        List<MovieReviewDto> movieReviews = movieReviewService.findAllByMovieId(movieId);
        return movieReviews;
    }

    @PostMapping("/review/write")
    public String receiveReview(@RequestBody ReviewRequest reviewRequest) {
        System.out.println("Received review: " + reviewRequest.getReviewContent());
        System.out.println("Received id: " + reviewRequest.getMovie());
        System.out.println("Received userId: " + reviewRequest.getUserId());
        System.out.println("Received rating: " + reviewRequest.getRating());

        String reviewContent = reviewRequest.getReviewContent();
        int rating = reviewRequest.getRating();
        long userId = reviewRequest.getUserId();
        Long movie = reviewRequest.getMovie();

        movieReviewService.saveReview(userId, reviewContent, rating, movie);

        return "리뷰가 성공적으로 저장되었습니다!";
    }
}
