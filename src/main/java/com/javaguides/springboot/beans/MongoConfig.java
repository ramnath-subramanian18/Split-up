//package com.javaguides.springboot.beans;
//
//import com.mongodb.MongoClientSettings;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//
//@Configuration
//public class MongoConfig {
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .build();
//
//        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(settings, "mydatabase"));
//    }
//}
