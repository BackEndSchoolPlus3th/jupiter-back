package com.jupiter.wyl.domain.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieMainDto {
    private Long id;
    private String title;
    private String overview;

    @JsonProperty("poster_path")
    public String posterPath;

    public MovieMainDto() {}

    public MovieMainDto(Long id, String title, String overview, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
    }
}
