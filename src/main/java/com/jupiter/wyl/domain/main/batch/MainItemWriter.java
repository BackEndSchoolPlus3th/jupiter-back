package com.jupiter.wyl.domain.main.batch;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.entity.MovieMain;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.stream.Collectors;

public class MainItemWriter implements ItemWriter<MovieMainDto> {

    private final MovieMainService movieMainService;

    public MainItemWriter(MovieMainService movieMainService) {
        this.movieMainService = movieMainService;
    }

    @Override
    public void write(Chunk<? extends MovieMainDto> items) throws Exception {
        // Chunk로 전달된 MovieMainDto 아이템을 List로 변환 후 DB에 저장
        List<? extends MovieMainDto> movieMainDtoList = items.getItems();

        // MovieMainDto를 MovieMain으로 변환 후 저장
        List<MovieMain> movieMainList = movieMainDtoList.stream()
                .map(dto -> new MovieMain(dto.getId(), dto.getTitle(), dto.getOverview(), dto.getPosterPath(), "popular"))
                .collect(Collectors.toList());

        // MovieMain을 MovieMainDto로 변환하여 DB에 저장
        List<MovieMainDto> movieMainDtoListToSave = movieMainList.stream()
                .map(movie -> new MovieMainDto(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath()))
                .collect(Collectors.toList());

        // DB에 저장하는 메서드 호출
        movieMainService.saveMoviesToDatabase(movieMainDtoListToSave, "popular");
    }
}