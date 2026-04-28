package ru.yandex.practicum.client.warehouse;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WarehouseFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new WarehouseFeignErrorDecoder();
    }
}
