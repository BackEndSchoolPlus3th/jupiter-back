package com.jupiter.wyl.domain.movie.movie.service;


import com.jupiter.wyl.domain.movie.movie.dto.response.*;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import com.jupiter.wyl.domain.movie.movie.repository.elastic.MovieSearchRepository;
import com.jupiter.wyl.domain.movie.movie.repository.jpa.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.jupiter.wyl.domain.movie.movie.service.MovieService.findCountryName;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieSearchService {
    @Autowired
    private final MovieSearchRepository movieSearchRepository; // Elasticsearch에서 검색

    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirector(String word) {
        List<MovieSearchDto> movieSearchDtos = new ArrayList<>();

        movieSearchRepository.findByTitleOrOverviewOrActorsOrDirector(word).forEach(movie ->
                movieSearchDtos.add(
                        MovieSearchDto.builder()
                                .id(movie.getId())
                                .overview(movie.getOverview())
                                .release_date(movie.getRelease_date() != null ? movie.getRelease_date().toString() : null) // 변환
                                .title(movie.getTitle())
                                .vote_average(movie.getVote_average())
                                .popularity(movie.getPopularity())
                                .poster_path(movie.getPoster_path())
                                .vote_count(movie.getVote_count())
                                .original_language(movie.getOriginal_language())
                                .original_country(findCountryName(movie.getOriginal_country()))
                                .genres("임시")
                                .build()
                )
        );

        return movieSearchDtos;
    }

}
