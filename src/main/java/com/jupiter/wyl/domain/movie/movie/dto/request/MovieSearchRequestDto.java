package com.jupiter.wyl.domain.movie.movie.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MovieSearchRequestDto {
    public String word;
}
