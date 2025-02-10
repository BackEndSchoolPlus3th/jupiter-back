package com.jupiter.wyl.domain.movie.movie.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MovieDto {
    Long id;
    private String overview;
    private LocalDate release_date;
    private String title;
    private float vote_average;
    private String director;
    private String popularity;
    private String poster_path;
    private int vote_count;
    private String original_language;
    private String original_country;
    private String actors;
    private String genres;
    private String keywords;
}
