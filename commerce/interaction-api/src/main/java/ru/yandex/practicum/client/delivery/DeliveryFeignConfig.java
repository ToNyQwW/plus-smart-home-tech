package ru.yandex.practicum.client.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class DeliveryFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper  objectMapper) {
        return new DeliveryFeignErrorDecoder(objectMapper);
    }
}
