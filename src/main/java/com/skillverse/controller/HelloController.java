package com.skillverse.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Hello", description = "Testing endpoints")
public class HelloController {

    @GetMapping("/hello")
    public Map<String,String> hello(){
        return Map.of("message", "hello world");
    }
}
