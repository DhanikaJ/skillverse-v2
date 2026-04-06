package com.skillverse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/")
    public Map<String, String> index() {
        return Map.of("message", "Skillverse API is running", "next", "/hello");
    }

    @GetMapping("/hello")
    public Map<String,String> hello(){
        return Map.of("message", "hello world");
    }
}
