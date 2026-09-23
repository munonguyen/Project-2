package com.devon.building;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringShoppingCart2Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringShoppingCart2Application.class, args);
    }
}

