package com.jupiter.wyl.domain.movie.movie.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "movie")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @Id
    private Long id; // API에서 제공하는 ID 사용

    private String overview;
    private String release_date;
    private String title;
    private float vote_average;
    private String status;
    private String director;
    private String popularity;
    private String poster_path;
    private String original_language;
    private String original_country;

    private String genres;
    private String keywords; // ⬅️ 키워드도 리스트로 변경

    private List<MovieReview> movieReviewList;

    private List<MovieGenre> movieGenreList;
    public void addMovieGenre(MovieGenre movieGenre) {
        if (movieGenreList != null) {
            movieGenreList.add(movieGenre);
        }
    }
    public List<String> getMovieGenres() {
        return movieGenreList.stream()
                .map(MovieGenre::getGenreName)
                .toList(); // ⬅️ 문자열이 아니라 리스트로 변환
    }
}