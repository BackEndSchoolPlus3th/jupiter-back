package com.jupiter.wyl.domain.main.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.dto.response.MovieMainResponse;
import com.jupiter.wyl.domain.main.entity.MovieMain;
import com.jupiter.wyl.domain.main.repository.MovieMainRepository;
import com.jupiter.wyl.domain.member.service.MemberService;
import com.jupiter.wyl.domain.movie.movie.document.Movie;
import com.jupiter.wyl.domain.movie.movie.repository.elastic.MovieSearchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieMainService {

    private static final Logger logger = LoggerFactory.getLogger(MovieMainService.class);
    private final ElasticsearchClient elasticsearchClient;

    @Value("${tmdb.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieMainRepository movieMainRepository;
    @Autowired
    private final MovieSearchRepository movieSearchRepository;
    private final MemberService memberService;
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // Popular 영화 가져오기
    public List<MovieMainDto> getPopularMovies() {
        return getMovies("popular");
    }

    // TopRated 영화 가져오기
    public List<MovieMainDto> getTopRatedMovies() {
        return getMovies("top_rated");
    }

    // 영화 데이터 가져오기: API에서 가져오거나 DB에서 가져오기
    public List<MovieMainDto> getMovies(String category) {
        List<MovieMain> movieMainList = movieMainRepository.findByCategory(category);
        List<MovieMainDto> movieMainDtos = movieMainList.stream()
                .map(movie -> new MovieMainDto(movie.getId(), movie.getTitle(), movie.getOverview(), movie.getPosterPath()))
                .collect(Collectors.toList());

        if (movieMainDtos.isEmpty()) {
            logger.info("DB에 데이터가 없어서 API에서 데이터를 가져옵니다. 카테고리: {}", category);
            movieMainDtos = fetchMoviesFromApi(category);  // API에서 데이터를 가져옴
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
            List<MovieMain> movieMainList = movieMainDtos.stream()
                    .map(dto -> new MovieMain(dto.getId(), dto.getTitle(), dto.getOverview(), dto.getPosterPath(), category))
                    .collect(Collectors.toList());

            movieMainRepository.saveAll(movieMainList);
            movieMainRepository.flush();
            logger.info("영화 데이터를 성공적으로 저장했습니다. 카테고리: {}", category);
        } catch (Exception e) {
            logger.error("DB 저장 실패: " + e.getMessage());
            throw new RuntimeException("영화 데이터를 저장하는 데 실패했습니다.", e);  // 예외를 던져서 트랜잭션 롤백 처리
        }
    }

    // API 요청을 공통화한 메서드 (Popular, Top_Rated)
    public List<MovieMainDto> fetchMoviesFromApi(String category) {
        try {
            List<MovieMainDto> allMovies = new ArrayList<>();  // 전체 영화 데이터를 저장할 리스트

            for (int page = 1; page <= 5; page++) {
                String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/movie/" + category)
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "ko-KR")
                        .queryParam("page", page)  // 페이지를 변경하여 1~5까지 가져옴
                        .toUriString();

                MovieMainResponse movieMainResponse = restTemplate.getForObject(url, MovieMainResponse.class);

                if (movieMainResponse != null && movieMainResponse.getResults() != null) {
                    allMovies.addAll(movieMainResponse.getResults().stream()
                            .map(movie -> new MovieMainDto(
                                    movie.getId(),
                                    movie.getTitle(),
                                    movie.getOverview(),
                                    movie.getPosterPath()))
                            .collect(Collectors.toList()));
                    logger.info("{} 영화 데이터를 API에서 성공적으로 가져왔습니다. 페이지: {}, 총 {}개의 영화.", category, page, movieMainResponse.getResults().size());
                }

                // 페이지마다 데이터를 합쳐서 저장
            }

            return allMovies;  // 모든 페이지의 데이터를 합쳐서 반환
        } catch (Exception e) {
            logger.error("API 호출 실패: {}", e.getMessage());
            throw new RuntimeException("영화 데이터를 가져오는 데 실패했습니다.", e);  // API 호출 실패 시 예외 던지기
        }
    }

    // 장르별 영화 데이터를 가져오는 메서드
    public List<MovieMainDto> getMoviesByGenre(String genreId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/discover/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("with_genres", genreId)  // 장르 ID로 필터링
                    .queryParam("language", "ko-KR")
                    .queryParam("region", "KR")  // 한국 지역으로 필터링
                    .queryParam("page", 1)
                    .queryParam("sort_by", "popularity.desc")  // 인기순으로 정렬
                    .toUriString();

            MovieMainResponse movieMainResponse = restTemplate.getForObject(url, MovieMainResponse.class);
            if (movieMainResponse != null) {
                logger.info("장르: {} 영화 데이터를 API에서 성공적으로 가져왔습니다. 총 {}개의 영화.", genreId, movieMainResponse.getResults().size());
            }
            return movieMainResponse != null ? movieMainResponse.getResults() : List.of();
        } catch (Exception e) {
            logger.error("API 호출 실패: {}", e.getMessage());
            throw new RuntimeException("영화 데이터를 가져오는 데 실패했습니다.", e);
        }
    }

    // 영화 데이터 가져오기: API에서 가져오거나 DB에서 가져오기
    public List<MovieMainDto> getMovies(String category) {
        List<MovieMainDto> movies = fetchMoviesFromApi(category);
        if (movies.isEmpty()) {
            // DB에 데이터가 없으면 API에서 데이터를 가져와서 저장
            logger.info("DB에 데이터가 없어서 API에서 데이터를 가져옵니다. 카테고리: {}", category);
            movies = fetchMoviesFromApi(category);
            saveMoviesToDatabase(movies, category);  // API에서 가져온 데이터 저장
        }
        return movies;
    }

    // Popular 영화 가져오기
    public List<MovieMainDto> getPopularMovies() {
        return getMovies("popular");
    }

    // TopRated 영화 가져오기
    public List<MovieMainDto> getTopRatedMovies() {
        return getMovies("top_rated");
    }

    // 스케줄러: 하루에 한 번만 API 호출 후 DB에 저장
    @Scheduled(cron = "0 00 00 * * ?")  // 매일 00:00에 실행
    @Transactional
    public void scheduledSaveMovies() {
        try {
            // 영화를 각각 가져와서 DB에 저장
            List<MovieMainDto> popularMovies = fetchMoviesFromApi("popular");
            saveMoviesToDatabase(popularMovies, "popular");

            List<MovieMainDto> topRatedMovies = fetchMoviesFromApi("top_rated");
            saveMoviesToDatabase(topRatedMovies, "top_rated");

            List<MovieMainDto> actionMovies = getMoviesByGenre("28");  // 액션 장르
            saveMoviesToDatabase(actionMovies, "action");

            List<MovieMainDto> comedyMovies = getMoviesByGenre("35");  // 코미디 장르
            saveMoviesToDatabase(comedyMovies, "comedy");

            List<MovieMainDto> romanceMovies = getMoviesByGenre("10749");  // 로맨스 장르
            saveMoviesToDatabase(romanceMovies, "romance");

            logger.info("영화 데이터를 스케줄러로 성공적으로 저장했습니다.");
        } catch (Exception e) {
            logger.error("스케줄러 작업 중 오류 발생: {}", e.getMessage());
        }
    }

    public List<MovieMainDto> defaultMoviesByGenre(String genre) throws IOException {
        logger.info(genre);
        SearchResponse<Movie> response = elasticsearchClient.search(s -> s
                        .index("movie_genres")  // 🔹 Elasticsearch에서 사용할 인덱스명 (변경 가능)
                        .query(q -> q
                                .bool(b -> b
                                        .should(f -> f.wildcard(m -> m.field("genres").value("*" + genre + "*"))) // ✅ 부분 일치 검색
                                )
                        )
                        .sort(SortOptions.of(sorts -> sorts
                                .field(fields -> fields.field("popularity").order(SortOrder.Desc))
                        ))
                        .size(10), // 🔹 최대 10개 가져오기
                Movie.class
        );

        return response.hits().hits().stream()
                .map(Hit::source).filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MovieMainDto> searchMoviesByGenre(String email, int index) throws IOException {
        String genre = memberService.getUserLikeGenres(email).split(",")[index];
        logger.info(genre);
        SearchResponse<Movie> response = elasticsearchClient.search(s -> s
                        .index("movie_genres")  // 🔹 Elasticsearch에서 사용할 인덱스명 (변경 가능)
                        .query(q -> q
                                .bool(b -> b
                                        .should(f -> f.wildcard(m -> m.field("genres").value("*" + genre + "*"))) // ✅ 부분 일치 검색
                                )
                        )
                        .sort(SortOptions.of(sorts -> sorts
                                .field(fields -> fields.field("popularity").order(SortOrder.Desc))
                        ))
                        .size(10), // 🔹 최대 10개 가져오기
                Movie.class
        );

        return response.hits().hits().stream()
                .map(Hit::source).filter(Objects::nonNull)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 🔹 Movie → MovieMainDto 변환 메서드
    private MovieMainDto convertToDto(Movie movie) {
        return new MovieMainDto(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPoster_path()
        );
    }
}