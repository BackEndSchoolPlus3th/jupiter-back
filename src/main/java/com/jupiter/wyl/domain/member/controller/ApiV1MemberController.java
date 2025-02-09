package com.jupiter.wyl.domain.member.controller;

import com.jupiter.wyl.domain.member.dto.request.MemberJoinRequest;
import com.jupiter.wyl.domain.member.dto.request.MemberLoginRequest;
import com.jupiter.wyl.domain.member.dto.response.MemberResponse;
import com.jupiter.wyl.domain.member.entity.Member;
import com.jupiter.wyl.domain.member.service.MemberService;
import com.jupiter.wyl.global.exception.ExceptionCode;
import com.jupiter.wyl.global.exception.ServiceException;
import com.jupiter.wyl.global.jwt.JwtProvider;
import com.jupiter.wyl.global.rsData.RsData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class ApiV1MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:5173")
    public RsData<MemberResponse> join(@Valid @RequestBody MemberJoinRequest userJoinRequest) {
        Member member = memberService.join(userJoinRequest.email(), userJoinRequest.nickname(), userJoinRequest.password());
        return new RsData<>("200", "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getNickname()), new MemberResponse(member));
    }

    // 로그인
    @PostMapping("/login")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    public RsData<Void> login(@Valid @RequestBody MemberLoginRequest memberLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.findByEmail(memberLoginRequest.email())
                .orElseThrow(() -> new ServiceException(ExceptionCode.USER_EMAIL_NOT_FOUND)); // 존재하지 않는 이메일 예외 처리

        if(!passwordEncoder.matches(memberLoginRequest.password(), member.getPassword())) {
            throw new ServiceException(ExceptionCode.USER_INVALID_PASSWORD); // 비밀번호 불일치 예외처리
        }
        System.out.println(memberLoginRequest.email());

        // 토큰 생성
        String token = jwtProvider.genAccessToken(member);
        // 응답 데이터에 accessToken 이름으로 토큰 발급 (쿠키 설정 : 보안, 경로, 유효기간)
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 로컬 테스트를 위해 false설정, 배포 시 true로 변경 필요
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        System.out.println(cookie.getName());
        System.out.println("쿠키를 추가합니다: " + cookie.getName() + "=" + cookie.getValue());
        // refreshToken 이름으로 토큰 발급
        String refreshToken = jwtProvider.genRefreshToken(member);
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        cookie.setSecure(false); // 로컬 테스트를 위해 false설정, 배포 시 true로 변경 필요
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60);
        response.addCookie(refreshCookie);
        System.out.println(refreshCookie.getName());

        return new RsData<>("200", "로그인 성공");
    }

    // 로그아웃
    @GetMapping("/logout")
    @CrossOrigin(origins = "http://localhost:5173")
    public RsData<Void> logout(HttpServletResponse response) {
        // 응답 데이터에 accessToken 이름으로 토큰을 발급
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Cookie refreshTokenCookie  = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        return new RsData<>("200", "로그아웃에 성공하였습니다.");
    }

    // 내 정보 조회
    @GetMapping("/me")
    @CrossOrigin(origins = "http://localhost:5173")
    public RsData<MemberResponse> me(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String accessToken = "";

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("accessToken")) {
                accessToken = cookie.getValue();
            }
        }

        Map<String, Object> claims = jwtProvider.getClaims(accessToken);
        String email = (String) claims.get("email");
        Member member = this.memberService.findByEmail(email).orElseThrow(() -> new ServiceException(ExceptionCode.USER_EMAIL_NOT_FOUND));
        return new RsData<>("200", "회원 정보 조회 성공", new MemberResponse(member));
    }
    
    // 쿠키 확인용 테스트
    @GetMapping("/cookie")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:5173")
    public RsData<Void> cookietest(HttpServletRequest request, HttpServletResponse response) {
        // 응답 데이터에 accessToken 이름으로 토큰 발급 (쿠키 설정 : 보안, 경로, 유효기간)
        Cookie cookie = new Cookie("Cookie22","test");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 로컬 테스트를 위해 false설정, 배포 시 true로 변경 필요
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        // refreshToken 이름으로 토큰 발급
        Cookie cookie2 = new Cookie("Cookie","test22");
        cookie2.setHttpOnly(true);
        cookie2.setSecure(false); // 로컬 테스트를 위해 false설정, 배포 시 true로 변경 필요
        cookie2.setPath("/");
        cookie2.setMaxAge(60 * 60);
        response.addCookie(cookie2);

        return new RsData<>("200", "쿠키생성 성공");
    }
}
