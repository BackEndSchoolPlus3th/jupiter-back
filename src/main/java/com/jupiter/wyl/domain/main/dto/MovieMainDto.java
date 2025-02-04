package com.jupiter.wyl.domain.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieMainDto {
    private Long id;
    private String title;
    private String overview;
    private String posterPath;

    @JsonProperty("poster_path")
    public String getPosterPath() {
        return posterPath;
    }

    public MovieMainDto() {}

    public MovieMainDto(Long id, String title, String overview, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
    }
}
