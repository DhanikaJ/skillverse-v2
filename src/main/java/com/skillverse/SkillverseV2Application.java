package com.skillverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.skillverse")
@RestController
public class SkillverseV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SkillverseV2Application.class, args);
    }

    @GetMapping
    public String helloWorld() {
        return "Hello Worlddddd";
    }

}
