package com.jupiter.wyl.domain.movie.movie.controller;


import com.jupiter.wyl.domain.movie.movie.dto.request.ReviewRequest;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieSearchDto;
import com.jupiter.wyl.domain.movie.movie.service.MovieSearchService;
import com.jupiter.wyl.domain.movie.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/movie")
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
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
        //System.out.println(movie.toString());
        return movie;
    }

//    @GetMapping("/review")
//    public MovieDto getMovieReview() {
//        MovieDto
//    }

    @PostMapping("/review/write")
    public String receiveReview(@RequestBody ReviewRequest reviewRequest) {
        System.out.println("Received review: " + reviewRequest.getContent());
        // db에 저장 필요
        return "리뷰가 성공적으로 저장되었습니다!";
    }
}
