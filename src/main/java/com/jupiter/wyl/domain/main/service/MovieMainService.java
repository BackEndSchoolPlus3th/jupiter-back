package com.jupiter.wyl.domain.main.service;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.dto.response.MovieMainResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class MovieMainService {

    @Value("${tmdb.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public MovieMainService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // Popular 영화 가져오기
    public List<MovieMainDto> getPopularMovies() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/movie/popular")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR")
                .queryParam("page", 1)
                .toUriString();

        MovieMainResponse movieMainResponse = restTemplate.getForObject(url, MovieMainResponse.class);
        return movieMainResponse != null ? movieMainResponse.getResults() : List.of();
    }

    // TopRated 영화 가져오기
    public List<MovieMainDto> getTopRatedMovies() {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/movie/top_rated")
                .queryParam("api_key", apiKey)
                .queryParam("language", "ko-KR")
                .queryParam("page", 1)
                .toUriString();

        MovieMainResponse movieMainResponse = restTemplate.getForObject(url, MovieMainResponse.class);
        return movieMainResponse != null ? movieMainResponse.getResults() : List.of();
    }
}