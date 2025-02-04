package com.jupiter.wyl.domain.main.dto.response;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import lombok.Data;

import java.util.List;

@Data
public class MovieMainResponse {
    private List<MovieMainDto> results;
}
