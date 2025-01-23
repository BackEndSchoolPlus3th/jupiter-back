package com.jupiter.wyl.domain.movie.movie.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="MovieController", description = "Movie")
public class MovieController {

    @GetMapping("/test")
    @ResponseBody
    public String controllerTest() {
        return "test";
    }

}
