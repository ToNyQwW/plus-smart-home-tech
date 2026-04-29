package ru.yandex.practicum.client.warehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class WarehouseFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new WarehouseFeignErrorDecoder(objectMapper);
    }
}
