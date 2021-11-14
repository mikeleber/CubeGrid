package com.frontbackend.thymeleaf.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static final String HZ_CLUSTER_NAME = "xdayconfig";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
