package com.jupiter.wyl.domain.movie.movie.repository.elastic;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovieSearchRepository {

    private final ElasticsearchClient elasticsearchClient;

    public List<Movie> searchByMultipleFields(String keyword) throws IOException {
        // Elasticsearch 검색 요청 생성
        SearchRequest request = SearchRequest.of(s -> s
                .index("movie") // 인덱스 이름
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh.match(m -> m.field("title.ngram").query(keyword))) // title.ngram 검색
                                .should(sh -> sh.match(m -> m.field("title.nori").query(keyword)))  // title.nori 검색
                                .should(sh -> sh.match(m -> m.field("director.ngram").query(keyword))) // director.ngram 검색
                        )
                )
        );

        // Elasticsearch 응답 처리
        SearchResponse<Movie> response = elasticsearchClient.search(request, Movie.class);

        // 검색 결과 반환 (문서 리스트)
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<Movie> searchByMultipleFieldsSortByPopular(String keyword) throws IOException {
        // Elasticsearch 검색 요청 생성
        SearchRequest request = SearchRequest.of(s -> s
                .index("movie") // 인덱스 이름
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh.match(m -> m.field("title.ngram").query(keyword))) // title.ngram 검색
                                .should(sh -> sh.match(m -> m.field("title.nori").query(keyword)))  // title.nori 검색
                                .should(sh -> sh.match(m -> m.field("director.ngram").query(keyword))) // director.ngram 검색
                        )
                ).sort(so -> so  // 정렬 옵션 추가
                        .field(f -> f
                                .field("popularity")
                                .order(SortOrder.Desc)
                        )
                )
        );

        // Elasticsearch 응답 처리
        SearchResponse<Movie> response = elasticsearchClient.search(request, Movie.class);

        // 검색 결과 반환 (문서 리스트)
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<Movie> searchByMultipleFieldsSortByLatest(String keyword) throws IOException {
        // Elasticsearch 검색 요청 생성
        SearchRequest request = SearchRequest.of(s -> s
                .index("movie") // 인덱스 이름
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh.match(m -> m.field("title.ngram").query(keyword))) // title.ngram 검색
                                .should(sh -> sh.match(m -> m.field("title.nori").query(keyword)))  // title.nori 검색
                                .should(sh -> sh.match(m -> m.field("director.ngram").query(keyword))) // director.ngram 검색
                        )
                ).sort(so -> so  // 정렬 옵션 추가
                        .field(f -> f
                                .field("release_date")
                                .order(SortOrder.Desc)
                        )
                )
        );

        // Elasticsearch 응답 처리
        SearchResponse<Movie> response = elasticsearchClient.search(request, Movie.class);

        // 검색 결과 반환 (문서 리스트)
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
