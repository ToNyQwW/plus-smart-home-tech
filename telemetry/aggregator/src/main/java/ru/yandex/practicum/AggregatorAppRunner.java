package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AggregatorAppRunner {
    public static void main(String[] args) {
        SpringApplication.run(AggregatorAppRunner.class, args);
    }
}