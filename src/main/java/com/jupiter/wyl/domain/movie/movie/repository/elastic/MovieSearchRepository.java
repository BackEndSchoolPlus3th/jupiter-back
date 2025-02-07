package com.jupiter.wyl.domain.movie.movie.repository.elastic;

import co.elastic.clients.elasticsearch.ml.Page;
import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieSearchDto;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;


import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository("movieSearchRepository")
public interface MovieSearchRepository extends ElasticsearchRepository<Movie, Long> {

    @Query("""
        {
            "bool": {
                "should": [
                    {
                        "match": {
                            "title": "#{#word}"
                        }
                    },
                    {
                        "wildcard": {
                            "overview": "*#{#word}*"
                        }
                    },
                    {
                        "match": {
                            "actors": "#{#word}"
                        }
                    },
                    {
                        "match": {
                            "director": "#{#word}"
                        }
                    }
                ]
            }
        }
    """)
    List<Movie> findByTitleOrOverviewOrActorsOrDirector(@Param("word") String word);

}