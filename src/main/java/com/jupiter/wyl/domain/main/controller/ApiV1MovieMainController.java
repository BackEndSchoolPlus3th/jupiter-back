package com.jupiter.wyl.domain.main.controller;

import com.jupiter.wyl.domain.main.dto.MovieMainDto;
import com.jupiter.wyl.domain.main.dto.MovieRecommandDto;
import com.jupiter.wyl.domain.main.service.MovieGenreMainService;
import com.jupiter.wyl.domain.main.service.MovieMainService;
import com.jupiter.wyl.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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
    private final MemberService memberService;

    @GetMapping("/api/v1/movie/popular")
    public List<MovieMainDto> getPopularMovies() {
        return movieMainService.getPopularMovies();
    }

    @GetMapping("/api/v1/movie/top-rated")
    public List<MovieMainDto> getTopRatedMovies() {
        return movieMainService.getTopRatedMovies();
    }

    @GetMapping("/api/v1/movie/genre/{genreId}")
    public List<MovieMainDto> getMoviesByGenre(@PathVariable String genreId) {
        return movieMainService.getMoviesByGenre(genreId);
    }

    @GetMapping("/api/v1/movie/likes_keyword")
    public List<MovieRecommandDto> getMoviesByLikeKeyword(HttpServletRequest request) throws IOException {
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
           return movieMainService.defaultMoviesByGenre("미스터리");
       }

        // JWT 토큰을 검증하고 이메일을 추출
        String email = null;
        try {
            email = memberService.getEmailFromAccessToken(accessToken).getUsername(); // 토큰에서 이메일을 추출하는 서비스 메서드 호출
            System.out.println("사용자 이메일: "+email);
        } catch (Exception e) {
            // 토큰이 유효하지 않거나 이메일 추출 실패 시 기본 영화 목록 반환
           return movieMainService.defaultMoviesByGenre("미스터리");
        }

        return movieMainService.searchMoviesByKeyword(email);  // 이메일을 기준으로 장르 영화 반환

    }

    @GetMapping("/api/v1/movie/likes")
    public List<MovieRecommandDto> getMoviesByLikeGenre(HttpServletRequest request) throws IOException {
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
    public List<MovieRecommandDto> getMoviesByLikeGenre_2nd(HttpServletRequest request) throws IOException {
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