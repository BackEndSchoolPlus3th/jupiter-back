package com.jupiter.wyl.domain.main.controller;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.service.MovieGenreMainService;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import com.jupiter.wyl.domain.member.entity.Member;
import com.jupiter.wyl.domain.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ApiV1MovieMainController {
    @Value("${custom.site.backUrl}")
    private String backUrl;

    @Value("${custom.site.frontUrl}")
    private String frontUrl;

    @GetMapping("/")
    public String main() {
        System.out.println("backUrl: " + backUrl);
        System.out.println("frontUrl: " + frontUrl);
        return "Hello, World!";
    }

    private final MovieMainService movieMainService;
    private final MovieGenreMainService movieGenreMainService;

    public ApiV1MovieMainController(MovieMainService movieMainService, MovieGenreMainService movieGenreMainService) {
        this.movieMainService = movieMainService;
        this.movieGenreMainService = movieGenreMainService;
    }
    private final MemberService memberService;


    @GetMapping("/api/v1/movie/popular")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getPopularMovies() {
        return movieMainService.getPopularMovies();
    }

    @GetMapping("/api/v1/movie/top-rated")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getTopRatedMovies() {
        return movieMainService.getTopRatedMovies();
    }

    @GetMapping("/api/v1/movie/genre/action")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getActionMovies() {
        return movieGenreMainService.getActionMovies();
    }

    @GetMapping("/api/v1/movie/genre/comedy")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getComedyMovies() {
        return movieGenreMainService.getComedyMovies();
    }

    @GetMapping("/api/v1/movie/genre/animation")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<MovieMainDto> getAnimationMovies() {
        return movieGenreMainService.getAnimationMovies();

    @GetMapping("/api/v1/movie/genre/{genreId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getMoviesByGenre(@PathVariable String genreId) {
        return movieMainService.getMoviesByGenre(genreId);
    }

    @GetMapping("/api/v1/movie/likes")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<MovieMainDto> getMoviesByLikeGenre(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        if (accessToken.isBlank()) {
            // 토큰이 없으면 기본 영화 목록 반환
            return movieMainService.defaultMoviesByGenre("액션");
        }

        // JWT 토큰을 검증하고 이메일을 추출
        String email = null;
        try {
            email = memberService.getEmailFromAccessToken(accessToken).getUsername(); // 토큰에서 이메일을 추출하는 서비스 메서드 호출
            System.out.println("사용자 이메일: "+email);
        } catch (Exception e) {
            // 토큰이 유효하지 않거나 이메일 추출 실패 시 기본 영화 목록 반환
            return movieMainService.defaultMoviesByGenre("액션");
        }

        System.out.println(email);  // 이메일 확인용 로그

        return movieMainService.searchMoviesByGenre(email, 0);  // 이메일을 기준으로 장르 영화 반환

    }

    @GetMapping("/api/v1/movie/likes_2nd")
    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    public List<MovieMainDto> getMoviesByLikeGenre_2nd(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        if (accessToken.isBlank()) {
            // 토큰이 없으면 기본 영화 목록 반환
            return movieMainService.defaultMoviesByGenre("애니메이션");
        }

        // JWT 토큰을 검증하고 이메일을 추출
        String email = null;
        try {
            email = memberService.getEmailFromAccessToken(accessToken).getUsername(); // 토큰에서 이메일을 추출하는 서비스 메서드 호출
            System.out.println("사용자 이메일: "+email);
        } catch (Exception e) {
            // 토큰이 유효하지 않거나 이메일 추출 실패 시 기본 영화 목록 반환
            return movieMainService.defaultMoviesByGenre("애니메이션");
        }

        System.out.println(email);  // 이메일 확인용 로그

        return movieMainService.searchMoviesByGenre(email, 1);  // 이메일을 기준으로 장르 영화 반환

    }

}