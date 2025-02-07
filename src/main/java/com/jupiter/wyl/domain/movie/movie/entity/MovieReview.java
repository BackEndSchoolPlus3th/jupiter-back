package com.jupiter.wyl.domain.movie.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.aot.generate.Generated;

import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
public class MovieReview {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String reviewContent;
    int rating;
    long userId;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

}
