package com.jupiter.wyl.domain.movie.movie.dto.response;

import lombok.Getter;

import java.util.List;
@Getter
public class MovieGenreResponseDto {
    private List<MovieGenreResultResponseDto> genres;
}
