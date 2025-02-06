package com.jupiter.wyl.domain.main.controller;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import com.jupiter.wyl.domain.member.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ApiV1MovieMainController {
    @Value("${custom.site.backUrl}")
    private String backUrl;

    @Value("${custom.site.frontUrl}")
    private String frontUrl;

    @GetMapping("/")
    public String main() {
        System.out.println("backUrl: " + backUrl);
        System.out.println("frontUrl: " + frontUrl);
        return "Hello, World!";
    }

    private final MovieMainService movieMainService;

    public ApiV1MovieMainController(MovieMainService movieMainService) {
        this.movieMainService = movieMainService;
    }

    @GetMapping("/api/v1/movie/popular")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getPopularMovies() {
        return movieMainService.getPopularMovies();
    }

    @GetMapping("/api/v1/movie/top-rated")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getTopRatedMovies() {
        return movieMainService.getTopRatedMovies();
    }

    @GetMapping("/api/v1/movie/genre/{genreId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getMoviesByGenre(@PathVariable String genreId) {
        return movieMainService.getMoviesByGenre(genreId);
    }

    @GetMapping("/api/v1/movie/likes")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getMoviesByLikeGenre(Principal principal) {
        return movieMainService.getMoviesByLikeGenre("apple@aaa.aaa");
    }
}