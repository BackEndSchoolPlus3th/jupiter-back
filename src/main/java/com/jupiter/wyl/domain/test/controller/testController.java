package com.jupiter.wyl.domain.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class testController {

        @GetMapping
        @ResponseBody
        public String hello() {
            return "hello";
        }
}
