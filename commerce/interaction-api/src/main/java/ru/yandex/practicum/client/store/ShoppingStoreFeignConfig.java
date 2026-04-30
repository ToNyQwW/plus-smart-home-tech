package ru.yandex.practicum.client.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class ShoppingStoreFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new ShoppingStoreFeignErrorDecoder(objectMapper);
    }
}
