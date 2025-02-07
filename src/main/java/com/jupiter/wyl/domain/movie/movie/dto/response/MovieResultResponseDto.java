package com.jupiter.wyl.domain.movie.movie.dto.response;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MovieResultResponseDto {
    private Long id;
    private String overview;
    private LocalDate release_date;
    private String title;
    private float vote_average;
    private List<Long> genre_ids;
    private String popularity;
    private String poster_path;
    private int vote_count;
    private String original_language;

}
