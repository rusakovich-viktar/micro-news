package com.example.newsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;


@TestConfiguration(proxyBeanMethods = false)
public class TestNewsProjectApplication {

//    @Bean
//    @ServiceConnection
//    PostgreSQLContainer<?> postgresContainer() {
//        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
//    }
//
//    @Bean
//    @ServiceConnection(name = "redis")
//    GenericContainer<?> redisContainer() {
//        return new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
//    }

    public static void main(String[] args) {
        SpringApplication.from(NewsProjectApplication::main).with(TestNewsProjectApplication.class).run(args);
    }

}
