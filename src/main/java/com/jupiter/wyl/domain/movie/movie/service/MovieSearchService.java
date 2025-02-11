package com.jupiter.wyl.domain.movie.movie.service;


import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.util.ObjectBuilder;
import co.elastic.clients.elasticsearch._types.SortOrder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jupiter.wyl.domain.movie.movie.service.MovieService.findCountryName;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieSearchService {
    @Autowired
    private final MovieSearchRepository movieSearchRepository; // Elasticsearch에서 검색

    private final ElasticsearchOperations elasticsearchOperations;

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
                                .original_language(movie.getOriginal_language())
                                .original_country(findCountryName(movie.getOriginal_country()))
                                .genres(movie.getGenres())
                                .build()
                )
        );

        return movieSearchDtos;
    }

    //인기순
    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirectorPopular(String word) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        boolQueryBuilder
                .should(new MatchQuery.Builder().field("title").query(word).build()._toQuery())
                .should(new MatchQuery.Builder().field("actor").query(word).build()._toQuery())
                .should(new MatchQuery.Builder().field("director").query(word).build()._toQuery())
                .should(new MatchQuery.Builder().field("actors").query(word).build()._toQuery())
                .should(new WildcardQuery.Builder().field("overview").value("*" + word + "*").build()._toQuery());

        Query query = boolQueryBuilder.build()._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withSort(s -> s.field(f -> f.field("popularity").order(SortOrder.Desc)))
                .build();


        List<MovieSearchDto> movieSearchDtos = elasticsearchOperations.search(searchQuery, Movie.class).getSearchHits().stream()
                .map(hit -> {
                    Movie movie = hit.getContent();
                    return MovieSearchDto.builder().
                            id(movie.getId()).
                            title(movie.getTitle()).
                            overview(movie.getOverview()).
                            release_date(movie.getRelease_date()).
                            popularity(movie.getPopularity()).
                           // genres(movie.getMovieGenres()).
                            original_country(findCountryName(movie.getOriginal_country())).
                          //  keywords(movie.getKeywords()).
                            poster_path(movie.getPoster_path()).
                         //   director(movie.getDirector()).
                            build();
                })
                .collect(Collectors.toList());
        return movieSearchDtos;
    }

    //최신순
    public List<MovieSearchDto> findByTitleOrOverviewOrActorsOrDirectorLatest(String word) {
        // Bool 쿼리 구성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        boolQueryBuilder
                .should(new MatchQuery.Builder().field("title").query(word).build()._toQuery())
                .should(new MatchQuery.Builder().field("actor").query(word).build()._toQuery())
                .should(new MatchQuery.Builder().field("director").query(word).build()._toQuery())
                .should(new MatchQuery.Builder().field("actors").query(word).build()._toQuery())
                .should(new WildcardQuery.Builder().field("overview").value("*" + word + "*").build()._toQuery());

        Query query = boolQueryBuilder.build()._toQuery();

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withSort(s -> s.field(f -> f.field("release_date").order(SortOrder.Desc)))
                .build();

        List<MovieSearchDto> movieSearchDtos = elasticsearchOperations.search(searchQuery, Movie.class).getSearchHits().stream()
                .map(hit -> {
                    Movie movie = hit.getContent();
                    return MovieSearchDto.builder().
                            id(movie.getId()).
                            title(movie.getTitle()).
                            overview(movie.getOverview()).
                            release_date(movie.getRelease_date()).
                            popularity(movie.getPopularity()).
                            // genres(movie.getMovieGenres()).
                                    original_country(findCountryName(movie.getOriginal_country())).
                            //  keywords(movie.getKeywords()).
                                    poster_path(movie.getPoster_path()).
                            //   director(movie.getDirector()).
                                    build();
                })
                .collect(Collectors.toList());
        return movieSearchDtos;
    }
}
