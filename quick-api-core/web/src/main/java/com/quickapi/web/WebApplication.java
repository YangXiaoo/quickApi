package com.quickapi.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quickcore.annotations.QuickApi;

@QuickApi(basePackages = "com.quickapi", localServiceName = "http://localhost:8899", projectName = "quickApi-test")
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
