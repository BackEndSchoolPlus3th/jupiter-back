package com.jupiter.wyl.domain.movie.movie.controller;


import com.jupiter.wyl.domain.movie.movie.document.Movie;
import com.jupiter.wyl.domain.movie.movie.dto.request.ReviewRequest;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieReviewDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieSearchDto;
import com.jupiter.wyl.domain.movie.movie.service.MovieSearchService;
import com.jupiter.wyl.domain.movie.movie.service.MovieService;
import com.jupiter.wyl.domain.movie.movie.service.MovieReviewService;
import com.jupiter.wyl.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/search/latest")
    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirectorLatest(@RequestParam("word") String word){
        List<MovieSearchDto> movieSearchDtos = movieSearchService.findByTitleOrOverviewOrActorsOrDirectorLatest(word);
        return movieSearchDtos;
    }

    @GetMapping("/{id}")
    public MovieDto getMovie(@PathVariable("id") Long id) {
        MovieDto movie = movieService.findById(id);
        return movie;
    }

    @GetMapping("/reviews/{movieId}")
    public List<MovieReviewDto> getMovieReview(@PathVariable("movieId") Long movieId) {
        List<MovieReviewDto> movieReviews = movieReviewService.findAllByMovieId(movieId);
        return movieReviews;
    }

    @PostMapping("/review/write")
    public String receiveReview(@RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal SecurityUser securityUser) {

        String reviewContent = reviewRequest.getReviewContent();
        int rating = reviewRequest.getRating();
        long userId = securityUser.getId();
        Long movie = reviewRequest.getMovie();

        movieReviewService.saveReview(userId, reviewContent, rating, movie);

        return "-----------------------review-write-success----------------------------";
    }
}
