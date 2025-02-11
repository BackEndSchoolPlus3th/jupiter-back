package com.jupiter.wyl.domain.movie.movie.service;

import com.jupiter.wyl.domain.movie.movie.dto.response.*;
import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.repository.jpa.MovieRepository;
import com.jupiter.wyl.domain.movie.movie.repository.jpa.MovieReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    @Autowired
    private final MovieReviewRepository movieReviewRepository;
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
                    release_date(e.getRelease_date()).
                    title(e.getTitle()).vote_average(e.getVote_average()).
                    status(status).poster_path(e.getPoster_path()).
                    original_country(!movieDetailResponseDto.getOriginCountry().isEmpty() ?
                            movieDetailResponseDto.getOriginCountry().getFirst() :"").
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
            movie.setGenresFromMovieGenres();
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
                                release_date(e.getRelease_date()).
                                vote_count(e.getVote_count()).
                                original_language(e.getOriginal_language()).
                                original_country(findCountryName(e.getOriginal_country())).
                                genres(Arrays.toString(e.getMovieGenreList().toArray())).
                                keywords(e.getKeywords()).
                                build()
                )
        );

        return movieDtos;
    }
    static String findCountryName(String code){
        if(code==null)
            return "";
        String countryName = switch (code) {
            case "KR" -> "대한민국";
            case "AF" -> "아프가니스탄";
            case "AM" -> "아르메니아";
            case "AZ" -> "아제르바이잔";
            case "BH" -> "바레인";
            case "BD" -> "방글라데시";
            case "BT" -> "부탄";
            case "CN" -> "중국";
            case "GE" -> "조지아";
            case "IN" -> "인도";
            case "GB" -> "영국";
            case "FR" -> "프랑스";
            case "DE" -> "독일";
            case "MX" -> "멕시코";
            case "ID" -> "인도네시아";
            case "RU" -> "러시아";
            case "ES" -> "스페인";
            case "IT" -> "이탈리아";
            case "JP" -> "일본";

            // 유럽
            case "AL" -> "알바니아";
            case "AD" -> "안도라";
            case "AT" -> "오스트리아";
            case "BE" -> "벨기에";
            case "BG" -> "불가리아";
            case "HR" -> "크로아티아";
            case "CY" -> "키프로스";

            // 아프리카
            case "DZ" -> "알제리";
            case "AO" -> "앙골라";
            case "BJ" -> "베냉";
            case "BW" -> "보츠와나";
            case "BF" -> "부르키나파소";
            case "BI" -> "부룬디";
            case "GH" -> "가나";

            // 아메리카
            case "US" -> "미국";
            case "CA" -> "캐나다";
            case "BR" -> "브라질";
            case "AR" -> "아르헨티나";
            case "CL" -> "칠레";
            case "CO" -> "콜롬비아";

            // 오세아니아
            case "AU" -> "호주";
            case "NZ" -> "뉴질랜드";
            case "FJ" -> "피지";
            case "PG" -> "파푸아뉴기니";

            // 특수 사례
            case "TW" -> "대만(중국 영토)";
            case "HK" -> "홍콩(중국 특별행정구)";
            case "MO" -> "마카오(중국 특별행정구)";
            default -> "알 수 없는 국가";
        };

        return countryName;
    }

    public MovieDto findById(Long id) {
        Movie movie = movieRepository.findById(id).get();
        MovieDto movieDto = MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .overview(movie.getOverview())
                .release_date(movie.getRelease_date())
                .vote_average(movie.getVote_average())
                .popularity(movie.getDirector())
                .poster_path(movie.getPoster_path())
                .vote_count(movie.getVote_count())
                .original_country(movie.getOriginal_country())
                .original_language(movie.getKeywords())
                .genres(movie.getGenres())
                .actors(movie.getActors())
                .director(movie.getDirector())
                .build();
        return movieDto;
    }



}
