package com.jupiter.wyl.domain.movie.movie.repository.elastic;

import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.elasticsearch.client.elc.Queries.matchQuery;

@Repository
@RequiredArgsConstructor
public class MovieSearchNativeRepository {

}
