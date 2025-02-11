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
    @CrossOrigin(origins = "https://www.wyl.seoez.site/", allowCredentials = "true")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No cookies found");
        }

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                return ResponseEntity.ok("Access Token found: " + cookie.getValue());
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Token not found");
    }
}