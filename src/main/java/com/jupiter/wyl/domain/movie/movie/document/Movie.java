package com.jupiter.wyl.domain.movie.movie.document;

import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@Document(indexName = "movie")
public class Movie {
    @Id
    Long id; //이미 id가 api 가 있어서 자동 할당 받을 필요가 없다고 생각했습니다.
    @Column(length = 512)
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

    @Column(length = 512)
    private String keywords;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenre> movieGenreList;

    @OneToMany
    List<MovieReview>  movieReviewList;

    public Movie(){

    }
    public void addMovieGenre(MovieGenre movieGenre) {
        movieGenreList.add(movieGenre);
    }

    public String getMovieGenresName(){
        StringBuilder sb = new StringBuilder();
        for(MovieGenre movieGenre : movieGenreList){
            sb.append(movieGenre.getGenreName());
            sb.append(",");
        }
        return sb.toString();
    }
}
