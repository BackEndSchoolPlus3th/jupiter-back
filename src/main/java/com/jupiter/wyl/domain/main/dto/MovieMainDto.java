package com.jupiter.wyl.domain.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MovieMainDto {
    private Long id;
    private String title;
    private String overview;
    private String posterPath;

    @JsonProperty("poster_path")
    public String getPosterPath() {
        return posterPath;
    }
}