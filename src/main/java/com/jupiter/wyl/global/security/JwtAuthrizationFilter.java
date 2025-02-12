package com.jupiter.wyl.global.security;

import com.jupiter.wyl.domain.member.service.MemberService;
import com.jupiter.wyl.global.rsData.RsData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthrizationFilter extends OncePerRequestFilter {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberService memberService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // 검증이 필요 없는 uri 처리
        if (request.getRequestURI().equals("/api/v1/member/login") ||
                request.getRequestURI().equals("/api/v1/member/logout") ||
                request.getRequestURI().equals("/api/v1/member/signup") ||
                request.getRequestURI().equals("/api/v1/movie/reviews/**") ||
                request.getRequestURI().equals("/api/*/auth/check")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = _getCookie("accessToken").orElse("");
        // accessToken 검증 or refreshToken 발급
        if (!accessToken.isBlank()) {
            // 토큰 유효기간 검증
            if (!memberService.validateToken(accessToken)) {
                // refreshToken 재발급 검증
                String refreshToken = _getCookie("refreshToken").orElse("");
                RsData<String> rs = memberService.refreshAccessToken(refreshToken);
                _addHeaderCookie("accessToken", rs.getData());
            }
            // securityUser 가져오기
            SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
            // 인가 처리
            SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
        }
        filterChain.doFilter(request, response);
    }
    private Optional<String> _getCookie(String name) {
        Cookie[] cookies = req.getCookies();

        if (cookies == null) {
            return Optional.empty(); // 쿠키가 없으면 빈 값 반환
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue);
    }
    private void _addHeaderCookie(String tokenName, String token) {
        ResponseCookie cookie = ResponseCookie.from(tokenName, token)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }
}
