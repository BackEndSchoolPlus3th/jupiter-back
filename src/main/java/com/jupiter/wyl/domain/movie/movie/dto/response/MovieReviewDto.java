package com.jupiter.wyl.domain.movie.movie.dto.response;

import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieReview;
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
    String userName;


    public static MovieReviewDto fromEntity(MovieReview movieReview, String userName) {
        return new MovieReviewDto(
                movieReview.getId(),
                movieReview.getReviewContent(),
                movieReview.getRating(),
                movieReview.getUserId(),
                movieReview.getMovie().getId(), // Movie 엔티티에서 ID 가져오기
                userName
        );
    }
}
