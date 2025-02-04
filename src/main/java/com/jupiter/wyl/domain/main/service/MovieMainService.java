package com.jupiter.wyl.domain.main.service;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.dto.response.MovieMainResponse;
import com.jupiter.wyl.domain.main.entity.MovieMain;
import com.jupiter.wyl.domain.main.repository.MovieMainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieMainService {

    private static final Logger logger = LoggerFactory.getLogger(MovieMainService.class);

    @Value("${tmdb.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieMainRepository movieMainRepository;

    public MovieMainService(RestTemplate restTemplate, MovieMainRepository movieMainRepository) {
        this.restTemplate = restTemplate;
        this.movieMainRepository = movieMainRepository;
    }

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // 영화 데이터를 DB에 저장
    @Transactional
    private void saveMoviesToDatabase(List<MovieMainDto> movieDtos, String category) {
        if (movieDtos.isEmpty()) {
            logger.warn("저장할 영화 데이터가 없습니다. 카테고리: {}", category);
            return;
        }
        try {
            List<MovieMain> movies = movieDtos.stream()
                    .map(dto -> new MovieMain(dto.getId(), dto.getTitle(), dto.getOverview(), dto.getPosterPath(), category))
                    .collect(Collectors.toList());
            movieMainRepository.saveAll(movies);
            movieMainRepository.flush(); // 저장한 데이터를 즉시 DB에 반영
        } catch (Exception e) {
            System.out.println("DB 저장 실패: " + e.getMessage());
            throw new RuntimeException("영화 데이터를 저장하는 데 실패했습니다.", e);  // 예외를 던져서 트랜잭션 롤백 처리
        }
    }

    // API 요청을 공통화한 메서드
    private List<MovieMainDto> fetchMoviesFromApi(String category) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/movie/" + category)
                    .queryParam("api_key", apiKey)
                    .queryParam("language", "ko-KR")
                    .queryParam("page", 1)
                    .toUriString();

            MovieMainResponse movieMainResponse = restTemplate.getForObject(url, MovieMainResponse.class);
            return movieMainResponse != null ? movieMainResponse.getResults() : List.of();
        } catch (Exception e) {
            // API 호출 실패 시 예외 처리
            logger.error("API 호출 실패: {}", e.getMessage());
            throw new RuntimeException("영화 데이터를 가져오는 데 실패했습니다.", e);  // API 호출 실패 시 예외 던지기
        }
    }

    // DB에서 영화 가져오기
    private List<MovieMainDto> getMoviesFromDatabase(String category) {
        List<MovieMain> movies = movieMainRepository.findByCategory(category);
        if (movies.isEmpty()) {
            logger.warn("DB에 저장된 영화 데이터가 없습니다. 카테고리: {}", category);
            return List.of();
        }
        // MovieMain을 MovieMainDto로 변환
        return movies.stream()
                .map(movie -> new MovieMainDto(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath()))
                .collect(Collectors.toList());
    }

    // Popular 영화 가져오기
    public List<MovieMainDto> getPopularMovies() {
        List<MovieMainDto> movies = fetchMoviesFromApi("popular");
        if (movies.isEmpty()) {
            // DB에 없으면 API에서 가져와서 저장
            movies = fetchMoviesFromApi("popular");
            saveMoviesToDatabase(movies, "popular");
        }
        return movies;
    }

    // TopRated 영화 가져오기
    public List<MovieMainDto> getTopRatedMovies() {
        List<MovieMainDto> movies = fetchMoviesFromApi("top_rated");
        if (movies.isEmpty()) {
            // DB에 없으면 API에서 가져와서 저장
            movies = fetchMoviesFromApi("top_rated");
            saveMoviesToDatabase(movies, "top_rated");
        }
        return movies;
    }
}