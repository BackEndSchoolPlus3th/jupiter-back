package com.jupiter.wyl.domain.movie.movie.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieReviewRequest {
    private Long id;
    private String reviewContent;
    private int rating;
    private String user;
    private Long movie;
}
