package com.medilabo.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AssessmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssessmentApplication.class, args);
    }
}