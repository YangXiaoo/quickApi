package com.quickapi.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import quickcore.annotations.QuickApi;
import com.quickapi.web.config.MyApplicationContextAware;
import quickcore.config.WebSocketConfig;
import quickcore.web.service.WebSocketService;


//@Import(value = { WebSocketConfig.class, WebSocketService.class, MyApplicationContextAware.class})
@QuickApi(basePackages = "com.quickapi.web", localServiceName = "http://localhost:8899", projectName = "quickApi-test")
@SpringBootApplication(scanBasePackages={"quickcore","com.quickapi"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
