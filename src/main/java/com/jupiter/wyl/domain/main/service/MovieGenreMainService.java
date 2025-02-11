package com.jupiter.wyl.domain.main.service;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.dto.response.MovieMainResponse;
import com.jupiter.wyl.domain.main.entity.MovieMain;
import com.jupiter.wyl.domain.main.repository.MovieMainRepository;
import com.jupiter.wyl.domain.movie.movie.repository.elastic.MovieSearchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieGenreMainService {

    private static final Logger logger = LoggerFactory.getLogger(MovieGenreMainService.class);

    @Value("${tmdb.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieMainRepository movieMainRepository;
    private final MovieSearchRepository movieSearchRepository;

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // 장르별 영화 데이터 가져오기
    public List<MovieMainDto> getActionMovies() {
        return getMoviesByCategory("action");  // 액션 장르
    }

    public List<MovieMainDto> getComedyMovies() {
        return getMoviesByCategory("comedy");  // 코미디 장르
    }

    public List<MovieMainDto> getAnimationMovies() {
        return getMoviesByCategory("animation");  // 애니메이션 장르
    }

    private static final Map<String, String> categoryMapping = new HashMap<>();

    static {
        categoryMapping.put("action", "28");
        categoryMapping.put("comedy", "35");
        categoryMapping.put("animation", "16");
    }

    // 장르별 영화 가져오기
    public List<MovieMainDto> getMoviesByCategory(String category) {
        List<MovieMain> movieMainList = movieMainRepository.findByCategory(category);
        List<MovieMainDto> movieMainDtos = movieMainList.stream()
                .map(movie -> new MovieMainDto(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath()))
                .collect(Collectors.toList());

        if (movieMainDtos.isEmpty()) {
            logger.info("DB에 카테고리 {} 영화 데이터가 없어서 API에서 가져옵니다.", category);
            movieMainDtos = fetchMoviesFromApi(category);  // API에서 카테고리별 영화 가져오기
            saveMoviesToDatabase(movieMainDtos, category);  // DB에 저장
        }
        return movieMainDtos;
    }

    // 영화 데이터 DB에 저장
    @Transactional
    public void saveMoviesToDatabase(List<MovieMainDto> movieMainDtos, String category) {
        if (movieMainDtos.isEmpty()) {
            logger.warn("저장할 영화 데이터가 없습니다. 카테고리: {}", category);
            return;
        }
        try {
            Set<Long> existingMovieIds = movieMainRepository.findByCategory(category).stream()
                    .map(MovieMain::getId)
                    .collect(Collectors.toSet());

            List<MovieMain> movieMainList = movieMainDtos.stream()
                    .filter(dto -> !existingMovieIds.contains(dto.getId()))
                    .map(dto -> new MovieMain(dto.getId(), dto.getTitle(), dto.getOverview(), dto.getPosterPath(), category))
                    .collect(Collectors.toList());

            if (!movieMainList.isEmpty()) {
                movieMainRepository.saveAll(movieMainList);
                movieMainRepository.flush();
                logger.info("영화 데이터를 성공적으로 저장했습니다. 카테고리: {}", category);
            }
        } catch (Exception e) {
            logger.error("DB 저장 실패: " + e.getMessage());
            throw new RuntimeException("영화 데이터를 저장하는 데 실패했습니다.", e);
        }
    }

    // API에서 영화 데이터 가져오기
    public List<MovieMainDto> fetchMoviesFromApi(String category) {
        String genreId = categoryMapping.get(category);
        if (genreId == null) {
            logger.error("알 수 없는 카테고리: {}", category);
            throw new RuntimeException("알 수 없는 카테고리입니다." + category);
        }

        try {
            List<MovieMainDto> allMovies = new ArrayList<>();
            Set<Long> seenMovieIds = new HashSet<>();

            for (int page = 1; page <= 5; page++) {
                String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/discover/movie")
                        .queryParam("api_key", apiKey)
                        .queryParam("with_genres", genreId)
                        .queryParam("language", "ko-KR")
                        .queryParam("region", "KR")
                        .queryParam("page", page)
                        .toUriString();

                MovieMainResponse movieMainResponse = restTemplate.getForObject(url, MovieMainResponse.class);
                if (movieMainResponse != null && movieMainResponse.getResults() != null) {
                    for (MovieMainDto movie : movieMainResponse.getResults()) {
                        if (!seenMovieIds.contains(movie.getId())) {
                            allMovies.add(movie);
                            seenMovieIds.add(movie.getId());
                        }
                    }
                    logger.info("카테고리 {} 영화 데이터를 API에서 성공적으로 가져왔습니다. 페이지: {}, 총 {}개의 영화.",
                            category, page, movieMainResponse.getResults().size());
                } else {
                    logger.info("카테고리 {} 영화 데이터가 없습니다. 페이지: {}", category, page);
                }
            }

            return allMovies;
        } catch (Exception e) {
            logger.error("API 호출 실패: 카테고리 {}: {}", category, e.getMessage());
            throw new RuntimeException("영화 데이터를 가져오는 데 실패했습니다.", e);
        }
    }

    // 스케줄러: 하루에 한 번만 API 호출 후 DB에 저장
    @Scheduled(cron = "0 00 00 * * ?")  // 매일 00:00에 실행
    @Transactional
    public void scheduledSaveMovies() {
        try {
            for (String category : categoryMapping.keySet()) {
                List<MovieMainDto> movies = getMoviesByCategory(category);
                saveMoviesToDatabase(movies, category);
            }

            logger.info("영화 데이터를 스케줄러로 성공적으로 저장했습니다.");
        } catch (Exception e) {
            logger.error("스케줄러 작업 중 오류 발생: {}", e.getMessage());
        }
    }
}
