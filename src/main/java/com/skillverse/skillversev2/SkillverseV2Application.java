package com.skillverse.skillversev2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.skillverse")
public class SkillverseV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SkillverseV2Application.class, args);
    }

}