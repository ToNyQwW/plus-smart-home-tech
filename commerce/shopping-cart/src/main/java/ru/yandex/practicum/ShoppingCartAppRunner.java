package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ShoppingCartAppRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartAppRunner.class, args);
    }
}