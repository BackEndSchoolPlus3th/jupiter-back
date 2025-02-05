package com.jupiter.wyl.domain.main.batch;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import org.springframework.batch.item.ItemProcessor;

public class MainItemProcessor implements ItemProcessor<MovieMainDto, MovieMainDto> {

    @Override
    public MovieMainDto process(MovieMainDto movieDto) throws Exception {
        return movieDto;
    }
}