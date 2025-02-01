package com.jupiter.wyl.domain.movie.movie.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class MovieResponseDto {

    private int page;
    private int total_pages;
    private int total_results;
    private List<MovieResultResponseDto> results;

}