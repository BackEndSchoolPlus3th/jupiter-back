package com.jupiter.wyl.domain.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieRecommandDto {
    private Long id;
    private String title;
    private String overview;
    private String genres;
    private String keywords;

    @JsonProperty("poster_path")
    public String posterPath;

    public MovieRecommandDto(Long id, String title, String overview, String genres, String keywords, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.genres = genres;
        this.keywords = keywords;
        this.posterPath = posterPath;
    }
}
