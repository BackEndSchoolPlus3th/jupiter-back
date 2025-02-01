package com.jupiter.wyl.domain.movie.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class MovieDetailResponseDto
{
    private int id;
    private String title;
    @JsonProperty("original_title")
    private String originalTitle;
    private String overview;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
    private long budget;
    private long revenue;
    private int runtime;
    private String status;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    private List<Genre> genres;
    private Credits credits;


    @Getter
    @Setter
    public static class Genre {
        private int id;
        private String name;

    }

    @Getter
    @Setter
    public static class Credits {
        private List<CastMember> cast;
        private List<CrewMember> crew;

    }

    @Getter
    @Setter
    public static class CastMember {
        private int id;
        private String name;
        private String character;
        @JsonProperty("profile_path")
        private String profilePath;
        private int order;

    }
    @Getter
    @Setter
    public static class CrewMember {
        private int id;
        private String name;
        private String department;
        private String job;
        @JsonProperty("profile_path")
        private String profilePath;

    }
}
