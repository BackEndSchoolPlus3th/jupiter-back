package com.jupiter.wyl.domain.movie.movie.dto.request;

import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long id;
    private String reviewContent;
    private int rating;
    private Long userId;
    private Long movie;
}
