package com.jupiter.wyl.domain.movie.movie.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.util.ObjectBuilder;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.*;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import com.jupiter.wyl.domain.movie.movie.repository.elastic.MovieSearchRepository;
import com.jupiter.wyl.domain.movie.movie.repository.jpa.MovieRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
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

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;

//    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirector(String word) {
//        List<MovieSearchDto> movieSearchDtos = new ArrayList<>();
//
//        movieSearchRepository.findByTitleOrOverviewOrActorsOrDirector(word).forEach(movie ->
//                movieSearchDtos.add(
//                        MovieSearchDto.builder()
//                                .id(movie.getId())
//                                .overview(movie.getOverview())
//                                .release_date(movie.getRelease_date() != null ? movie.getRelease_date().toString() : null) // 변환
//                                .title(movie.getTitle())
//                                .vote_average(movie.getVote_average())
//                                .popularity(movie.getPopularity())
//                                .poster_path(movie.getPoster_path())
//                                .original_language(movie.getOriginal_language())
//                                .original_country(findCountryName(movie.getOriginal_country()))
//                                .genres(movie.getGenres())
//                                .build()
//                )
//        );
//
//        return movieSearchDtos;
//    }
//    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirector(String word) throws IOException {
//
//        SearchResponse<Movie> response = elasticsearchClient.search(s -> s
//                        .index("movie")
//                        .query(q -> q
//                                .multiMatch(m -> m
//                                        .query(word) // 검색어
//                                        .fields("title", "actors", "overview","director") // 검색할 필드들
//                                 //       .fuzziness("1")
//
//                                        .prefixLength(2)
//                                        .type(TextQueryType.BestFields) // BEST_FIELDS 타입 사용
//                                )
//                        )
//                        .collapse(c -> c
//                                .field("title.keyword"))
//                        .size(10)
////                        .collapse(c -> c
////                                .field("title.keyword")
////                                .innerHits(i -> i.name("grouped_hits").size(1)))
//                        , Movie.class);
//
//
//        return response.hits().hits().stream()
//                .map(Hit::source).filter(Objects::nonNull)
//                .map(this::convertToMovieSearchDto)
//                .collect(Collectors.toList());
//    }
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
