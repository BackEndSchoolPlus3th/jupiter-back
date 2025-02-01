package com.jupiter.wyl.domain.movie.movie.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
public class MovieKeywordsDto {

       private List<Keyword> keywords;

    @Getter
    @Setter
    public static class Keyword {
        private int id;
        private String name;

    }
}
