package ru.yandex.practicum.client.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.exception.delivery.DeliveryNotFoundException;
import ru.yandex.practicum.exception.delivery.DeliveryServiceUnavailableException;

import java.io.InputStream;

import static ru.yandex.practicum.util.ErrorMessagesConstants.DELIVERY_ALREADY_EXISTS;
import static ru.yandex.practicum.util.ErrorMessagesConstants.DELIVERY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class DeliveryFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {

        try (InputStream inputStream = response.body().asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 400, 404 -> mapWarehouseException(error);
                default -> new DeliveryServiceUnavailableException(error.getUserMessage());
            };
        }
    }

    private RuntimeException mapWarehouseException(ErrorResponse error) {
        return switch (error.getMessage()) {
            case DELIVERY_NOT_FOUND -> new DeliveryNotFoundException(error.getUserMessage());
            case DELIVERY_ALREADY_EXISTS -> new DeliveryAlreadyExistsException(error.getUserMessage());
            default -> new RuntimeException(error.getUserMessage());
        };
    }
}
