package com.jupiter.wyl.domain.movie.movie.controller;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieDto;
import com.jupiter.wyl.domain.movie.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/movie")
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<MovieDto> findAllMovies(){
        List<MovieDto> movieDtos = movieService.findAll();
        return movieDtos;
    }

    @GetMapping("/search")
    public List<MovieDto> findByTitle(){
        List<MovieDto> movieSearchDtos = movieService.searchByTitle("글래디에이터");
        return movieSearchDtos;
    }

}
