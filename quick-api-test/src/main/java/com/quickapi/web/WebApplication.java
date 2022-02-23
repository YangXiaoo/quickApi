package com.quickapi.web;

import com.quickapi.web.config.MyApplicationContextAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import quickcore.config.WebSocketConfiguration;
import quickcore.web.service.WebSocketService;


//@Import(value = { WebSocketConfiguration.class })
//@QuickApi(basePackages = "com.quickapi.web", localServiceName = "http://localhost:8899", projectName = "quickApi-test")
//@SpringBootApplication(scanBasePackages={"quickcore"}) //
@SpringBootApplication()
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
