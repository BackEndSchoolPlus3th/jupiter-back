package com.jupiter.wyl.domain.main.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return ResponseEntity.ok("쿠키가 없습니다.");
        }

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                return ResponseEntity.ok("Access Token found: " + cookie.getValue());
            }
        }

        return ResponseEntity.ok("쿠키가 없습니다."); // 500 에러가 아닌 OK 응답
    }
}