package com.jupiter.wyl.domain.movie.movie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jupiter.wyl.domain.movie.movie.dto.response.*;
import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreService movieGenreService;
    @Value("${tmdb.key}")
    private String key;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveDummyData(MovieResponseDto movieResponseDto) throws IOException {

        List<MovieResultResponseDto> results = movieResponseDto.getResults();

        for(MovieResultResponseDto e : results){
            RestTemplate restTemplate = new RestTemplate();

            MovieDetailResponseDto movieDetailResponseDto =
                    restTemplate.getForObject("https://api.themoviedb.org/3/movie/"+e.getId()+"?append_to_response=credits&language=ko-KR&api_key="+key,MovieDetailResponseDto.class);
            MovieKeywordsDto movieKeywordsDto =
                    restTemplate.getForObject("https://api.themoviedb.org/3/movie/"+e.getId()+"/keywords?language=ko-KR&api_key="+key,MovieKeywordsDto.class);

            StringBuilder keywords = new StringBuilder();
            String status = movieDetailResponseDto.getStatus();
            for(MovieKeywordsDto.Keyword keyword:movieKeywordsDto.getKeywords()){
                keywords.append(keyword.getName()).append(",");
                if(keywords.length()>490)
                    break;
            }

            List< MovieDetailResponseDto.CrewMember> crews = movieDetailResponseDto.getCredits().getCrew();
            String director="";
            if(crews!=null){
                for(MovieDetailResponseDto.CrewMember crew: crews){
                    if(crew.getJob().equals("Director")) {
                        director = crew.getName();
                        break;
                    }
                }
            }


            StringBuilder actors = new StringBuilder();
            List<MovieDetailResponseDto.CastMember> casts = movieDetailResponseDto.getCredits().getCast();
            if(casts!=null){
                for(MovieDetailResponseDto.CastMember castMember : casts){
                    actors.append(castMember.getName()).append(",");
                    if(actors.length()>490)
                        break;
                }
            }


            Movie movie = Movie.builder().id(e.getId()).
                    overview(e.getOverview().length()>512?e.getOverview().substring(512):e.getOverview()).
                    release_date(e.getRelease_date()).title(e.getTitle()).vote_average(e.getVote_average()).
                    status(status).
                    keywords(keywords.toString()).
                    actors(actors.toString()).director(director).
                    movieGenreList(new ArrayList<>()).
                    movieReviewList(new ArrayList<>()).popularity(e.getPopularity()).original_language(e.getOriginal_language()).
                    build();

            List<Long> genre_ids = e.getGenre_ids();
            for(Long genre:genre_ids){
                MovieGenre movieGenre = MovieGenre.builder().
                        genreName(movieGenreService.getValue(genre)).build();
                movie.addMovieGenre(movieGenre);
            }
            movieRepository.save(movie);

        }

        System.out.println("더미 데이터 저장");
    }

    @Transactional
    public List<MovieDto> findAll(){
        List<MovieDto> movieDtos = new ArrayList<>();
        movieRepository.findAll().forEach(e->
                movieDtos.add(
                        MovieDto.builder().
                                id(e.getId()).
                                overview(e.getOverview()).
                                title(e.getTitle()).
                                vote_average(e.getVote_average()).
                                popularity(e.getPopularity()).
                                poster_path(e.getPoster_path()).
                                vote_count(e.getVote_count()).
                                original_language(e.getOriginal_language()).
                                genres(Arrays.toString(e.getMovieGenreList().toArray())).
                                build()
                )
        );

        return movieDtos;
    }


}
