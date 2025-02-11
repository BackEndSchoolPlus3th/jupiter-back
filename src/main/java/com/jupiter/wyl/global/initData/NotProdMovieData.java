package com.jupiter.wyl.global.initData;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieGenreResponseDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieResponseDto;
import com.jupiter.wyl.domain.movie.movie.service.MovieGenreService;
import com.jupiter.wyl.domain.movie.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;


@Configuration
//@Profile("!prod")
public class NotProdMovieData {
    @Value("${tmdb.key}")
    private String key;

    @Bean
    public ApplicationRunner initNotProd(MovieService movieService, MovieGenreService movieGenreService) {
        return args -> {
            RestTemplate restTemplate = new RestTemplate();

                MovieGenreResponseDto genreResponseDto =
                        restTemplate.getForObject("https://api.themoviedb.org/3/genre/movie/list?language=ko&api_key="+key,MovieGenreResponseDto.class);

                movieGenreService.saveDummyData(genreResponseDto);


            for(int i=1;i<=10;i++){
                MovieResponseDto movieResponse =
                        restTemplate.getForObject("https://api.themoviedb.org/3/movie/popular?api_key="+key+"&language=ko-KR&page="+i, MovieResponseDto.class);

                movieService.saveDummyData(movieResponse);
            }

        };
    }
}
