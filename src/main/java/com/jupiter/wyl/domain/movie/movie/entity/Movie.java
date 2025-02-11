package com.jupiter.wyl.domain.movie.movie.entity;

import com.jupiter.wyl.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class Movie {
    @Id
    Long id; //이미 id가 api 가 있어서 자동 할당 받을 필요가 없다고 생각했습니다.
    @Column(length = 512)
    private String overview;
    private LocalDate release_date;
    private String title;
    private float vote_average;

    private String popularity;
    private String poster_path;
    private int vote_count;
    private String original_language;
    @Column(length = 512)
    private String keywords;
    private String status;
    @Column(length = 512)
    private String actors;
    private String director;
    private String original_country;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenre> movieGenreList;

    @Column(length = 512)
    private String genres;  // String으로 정의한 genres 필드 추가

    @OneToMany(mappedBy = "movie", cascade = ALL, orphanRemoval = true)
    List<MovieReview>  movieReviewList;

    public Movie(){

    }

    public void addMovieGenre(MovieGenre movieGenre) {
        movieGenreList.add(movieGenre);
        movieGenre.setMovie(this);
    }

    public String getMovieGenres(){
        StringBuilder sb = new StringBuilder();
        for(MovieGenre movieGenre : movieGenreList){
            sb.append(movieGenre.getGenreName());
            sb.append(",");
        }
        return sb.toString();
    }

    // genres 필드를 자동으로 설정
    public void setGenresFromMovieGenres() {
        StringBuilder sb = new StringBuilder();
        for (MovieGenre movieGenre : movieGenreList) {
            sb.append(movieGenre.getGenreName());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);  // 마지막 콤마 제거
        }
        this.genres = sb.toString();
    }
}
