package com.jupiter.wyl.domain.main.controller;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieGenreMainService;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final MovieGenreMainService movieGenreMainService;

    public ApiV1MovieMainController(MovieMainService movieMainService, MovieGenreMainService movieGenreMainService) {
        this.movieMainService = movieMainService;
        this.movieGenreMainService = movieGenreMainService;
    }

    @GetMapping("/api/v1/movie/popular")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getPopularMovies() {
        return movieMainService.getPopularMovies();
    }

    @GetMapping("/api/v1/movie/top-rated")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getTopRatedMovies() {
        return movieMainService.getTopRatedMovies();
    }

    @GetMapping("/api/v1/movie/genre/action")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getActionMovies() {
        return movieGenreMainService.getActionMovies();
    }

    @GetMapping("/api/v1/movie/genre/comedy")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getComedyMovies() {
        return movieGenreMainService.getComedyMovies();
    }

    @GetMapping("/api/v1/movie/genre/animation")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getAnimationMovies() {
        return movieGenreMainService.getAnimationMovies();
    }
}