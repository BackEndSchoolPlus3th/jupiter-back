package com.jupiter.wyl.domain.movie.movie.service;

import com.jupiter.wyl.domain.movie.movie.dto.response.MovieGenreResponseDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieGenreResultResponseDto;
import com.jupiter.wyl.domain.movie.movie.dto.response.MovieResponseDto;
import com.jupiter.wyl.domain.movie.movie.entity.Movie;
import com.jupiter.wyl.domain.movie.movie.entity.MovieGenre;
import com.jupiter.wyl.domain.movie.movie.repository.MovieGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;

    private final HashMap<Long,String> genreInfo=new HashMap<>();

    public void saveDummyData(MovieGenreResponseDto movieGenreResponseDto){
        List<MovieGenre> movieGenres = new ArrayList<>();
        movieGenreResponseDto.getGenres()
                .forEach(e->
                        genreInfo.put(e.getId(),e.getName())
                );
        System.out.println("더미 데이터 저장");
    }
    public String getValue(Long id){
        return genreInfo.get(id);
    }
}
