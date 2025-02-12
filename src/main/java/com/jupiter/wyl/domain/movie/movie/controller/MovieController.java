package com.jupiter.wyl.domain.movie.movie.controller;


import com.jupiter.wyl.domain.member.service.MemberService;
import com.jupiter.wyl.domain.movie.movie.dto.request.MovieReviewRequest;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieReviewDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieSearchDto;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
import com.jupiter.wyl.domain.movie.movie.service.MovieSearchService;
import com.jupiter.wyl.domain.movie.movie.service.MovieService;
import com.jupiter.wyl.domain.movie.movie.service.MovieReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/movie")
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieReviewService movieReviewService;
    private final MovieSearchService movieSearchService;
    private final MemberService memberService;

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

    // 영화 상세 페이지 조회
    @GetMapping("/{id}")
    public MovieDto getMovie(@PathVariable("id") Long id) {
        MovieDto movie = movieService.findById(id);
        return movie;
    }

    // 모든 리뷰 조회
    @GetMapping("/reviews/{movieId}")
    public List<MovieReviewDto> getMovieReview(@PathVariable("movieId") Long movieId) {
        List<MovieReviewDto> movieReviews = movieReviewService.findAllByMovieId(movieId);
        System.out.println(movieReviews);
        return movieReviews;
    }

    //리뷰 작성 하기
    @PostMapping("/review/write")
    public String receiveReview(@RequestBody MovieReviewRequest reviewRequest) {
        String userEmail = reviewRequest.getUser();

        Long userId = memberService.getUserIdByEmail(userEmail);

        String reviewContent = reviewRequest.getReviewContent();
        int rating = reviewRequest.getRating();
        Long movie = reviewRequest.getMovie();

        movieReviewService.saveReview(userId, reviewContent, rating, movie);

        return "-----------------------review-write-success----------------------------";
    }

    // 로그인 한 회원이 쓴 리뷰 조회
    @GetMapping("/review/{userEmail}/{movieId}")
    public ResponseEntity<MovieReviewDto> getMovieReviewByEmail(@PathVariable("userEmail") String userEmail, @PathVariable("movieId") Long movieId) {

        System.out.println("User Email: " + userEmail);
        System.out.println("Movie ID: " + movieId);

        Long userId = memberService.getUserIdByEmail(userEmail);

        MovieReviewDto reviewDto = movieReviewService.getReviewByUserAndMovie(userId, movieId);
        return ResponseEntity.ok(reviewDto);
    }

    // 리뷰 수정
    @PutMapping("/review/update/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody MovieReviewRequest moviereviewRequest) {

        System.out.println(reviewId);
        System.out.println("new Content: " + moviereviewRequest.getReviewContent());
        System.out.println("new rating: " + moviereviewRequest.getRating());

        movieReviewService.updateReview(reviewId, moviereviewRequest.getReviewContent(), moviereviewRequest.getRating());
        return ResponseEntity.ok("리뷰 수정 성공");
    }

}
