package com.javaguides.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.javaguides.springboot.Controller.*"})
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "com.javaguides.springboot.repositories")

public class SpringbootApplication {


    public static void main(String[] args) {
        System.out.println("Starting");
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
