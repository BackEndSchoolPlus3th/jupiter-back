package com.jupiter.wyl.domain.main.controller;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import org.springframework.beans.factory.annotation.Value;
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

    public ApiV1MovieMainController(MovieMainService movieMainService) {
        this.movieMainService = movieMainService;
    }

    @GetMapping("/api/movies/popular")
    public List<MovieMainDto> getPopularMovies() {
        return movieMainService.getPopularMovies();
    }

    @GetMapping("/api/movies/top-rated")
    public List<MovieMainDto> getTopRatedMovies() {
        return movieMainService.getTopRatedMovies();
    }
}
