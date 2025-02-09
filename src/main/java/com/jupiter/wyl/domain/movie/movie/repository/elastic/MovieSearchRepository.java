package com.jupiter.wyl.domain.movie.movie.repository.elastic;

import com.jupiter.wyl.domain.movie.movie.document.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
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

    @Query("""
       {
                 "sort": [
                   { "popularity.numeric": { "order": "desc" }}  // 정렬 문법 수정
                 ],
                 "query": {
                   "bool": {
                     "should": [  // OR 조건
                       { "match": { "title": "#{#word}" } },
                       { "wildcard": { "overview": "*#{#word}*" } },
                       { "match": { "actors": "#{#word}" } },
                       { "match": { "director": "#{#word}" } }
                     ],
                     "minimum_should_match": 1
                   }
                 }
       }
    """)
    List<Movie> findByTitleOrOverviewOrActorsOrDirectorPopular(@Param("word") String word);

}