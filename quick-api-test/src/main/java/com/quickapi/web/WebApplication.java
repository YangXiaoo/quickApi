package com.quickapi.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@Import(value = { WebSocketConfiguration.class })
//@QuickApi(basePackages = "com.quickapi.socket", localServiceName = "http://localhost:8899", projectName = "quickApi-test")
//@SpringBootApplication(scanBasePackages={"quickcore"}) //
@SpringBootApplication()
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
