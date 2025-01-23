package com.jupiter.wyl.domain.movie.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MovieController {

    @GetMapping("/test")
    @ResponseBody
    public String controllerTest() {
        return "test";
    }

}
