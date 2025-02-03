package com.jupiter.wyl.domain.movie.movie.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.aot.generate.Generated;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class MovieReview {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String reviewContent;
    int rating;

    long userId;

}
