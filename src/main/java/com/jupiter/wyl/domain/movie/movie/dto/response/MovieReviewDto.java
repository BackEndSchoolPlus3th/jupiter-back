package com.jupiter.wyl.domain.movie.movie.dto.response;

import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovieReviewDto {
    Long id;
    String reviewContent;
    int rating;
    long userId;
    Long movie;
}
