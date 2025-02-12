package com.jupiter.wyl.domain.movie.movie.repository.elastic;

import com.jupiter.wyl.domain.movie.movie.document.Movie;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

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