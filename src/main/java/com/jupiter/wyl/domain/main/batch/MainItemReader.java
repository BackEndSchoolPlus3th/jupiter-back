package com.jupiter.wyl.domain.main.batch;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class MainItemReader implements ItemReader<MovieMainDto> {

    private final MovieMainService movieMainService;
    private Iterator<MovieMainDto> currentIterator;

    public MainItemReader(MovieMainService movieMainService) {
        this.movieMainService = movieMainService;
    }

    @Override
    public MovieMainDto read() throws Exception {
        if (currentIterator == null || !currentIterator.hasNext()) {
            List<MovieMainDto> movieMainDtos = movieMainService.getPopularMovies();  // 예시로 인기 영화 목록을 가져옴
            currentIterator = movieMainDtos.iterator();
        }
        return currentIterator != null && currentIterator.hasNext() ? currentIterator.next() : null;
    }
}