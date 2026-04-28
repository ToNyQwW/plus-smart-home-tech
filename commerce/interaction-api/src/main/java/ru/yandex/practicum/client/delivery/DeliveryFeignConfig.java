package ru.yandex.practicum.client.delivery;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class DeliveryFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DeliveryFeignErrorDecoder();
    }
}
