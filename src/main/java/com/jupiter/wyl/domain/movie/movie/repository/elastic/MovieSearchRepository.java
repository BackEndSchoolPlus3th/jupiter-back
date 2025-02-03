package com.jupiter.wyl.domain.movie.movie.repository.elastic;

import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository("movieSearchRepository")
public interface MovieSearchRepository extends ElasticsearchRepository<Movie, Long> {
    List<Movie> findByTitleContaining(String title);

}