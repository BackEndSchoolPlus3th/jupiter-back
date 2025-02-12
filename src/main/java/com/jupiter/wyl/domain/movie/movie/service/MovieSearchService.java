package com.jupiter.wyl.domain.movie.movie.service;



import co.elastic.clients.elasticsearch.ElasticsearchClient;

import com.jupiter.wyl.domain.movie.movie.dto.response.*;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import com.jupiter.wyl.domain.movie.movie.repository.elastic.MovieSearchRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieSearchService {
    @Autowired
    private final MovieSearchRepository movieSearchRepository; // Elasticsearch에서 검색

public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirector(String word) throws IOException {

    List<Movie> movies = movieSearchRepository.searchByMultipleFields(word);
    List<MovieSearchDto> movieSearchDtos = new ArrayList<>();
    movies.forEach(movie ->
            movieSearchDtos.add(convertToMovieSearchDto(movie))
        );
    return movieSearchDtos;
}

    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirectorPopular(String word) throws IOException {
        List<Movie> movies = movieSearchRepository.searchByMultipleFieldsSortByPopular(word);
        List<MovieSearchDto> movieSearchDtos = new ArrayList<>();
        movies.forEach(movie ->
                movieSearchDtos.add(convertToMovieSearchDto(movie))
        );
        return movieSearchDtos;
    }

public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirectorLatest(String word) throws IOException {
    List<Movie> movies = movieSearchRepository.searchByMultipleFieldsSortByLatest(word);
    List<MovieSearchDto> movieSearchDtos = new ArrayList<>();
    movies.forEach(movie ->
            movieSearchDtos.add(convertToMovieSearchDto(movie))
    );
    return movieSearchDtos;
}
    private MovieSearchDto convertToMovieSearchDto(Movie movie){
        return MovieSearchDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genres(movie.getGenres())
                .original_language(movie.getOriginal_language())
                .original_country(movie.getOriginal_country())
                .popularity(movie.getPopularity())
                .poster_path(movie.getPoster_path())
                .release_date(movie.getRelease_date())
                .build();
    }
}
