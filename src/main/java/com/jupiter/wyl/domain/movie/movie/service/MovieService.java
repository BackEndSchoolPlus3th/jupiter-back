package com.jupiter.wyl.domain.movie.movie.service;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieResponseDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieResultResponseDto;
import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreService movieGenreService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveDummyData(MovieResponseDto movieResponseDto){

        List<MovieResultResponseDto> results = movieResponseDto.getResults();

        for(MovieResultResponseDto e : results){
            Movie movie = Movie.builder().id(e.getId()).
                    overview(e.getOverview().length()>512?e.getOverview().substring(512):e.getOverview()).
                    release_date(e.getRelease_date()).title(e.getTitle()).vote_average(e.getVote_average()).
                    movieGenreList(new ArrayList<>()).
                    movieReviewList(new ArrayList<>()).popularity(e.getPopularity()).original_language(e.getOriginal_language()).
                    build();
             //genre 얻어 오기
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

}
