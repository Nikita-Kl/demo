package com.example.sweater;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class Application {

    public static void main(String[] args) {
        System.setProperty("server.port", "8085");
        SpringApplication.run(Application.class, args);
    }

}
