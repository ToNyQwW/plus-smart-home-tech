package ru.yandex.practicum.client.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.exception.order.OrderServiceUnavailableException;

import java.io.InputStream;

import static ru.yandex.practicum.util.ErrorMessagesConstants.ORDER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class OrderFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {
        Response.Body body = response.body();
        throwIfEmptyBodyResponse(body);

        try (InputStream inputStream = body.asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 404 -> mapOrderException(error);
                default -> new OrderServiceUnavailableException(error.getUserMessage());
            };
        }
    }

    private RuntimeException mapOrderException(ErrorResponse error) {
        return switch (error.getMessage()) {
            case ORDER_NOT_FOUND -> new OrderNotFoundException(error.getUserMessage());
            default -> new RuntimeException(error.getUserMessage());
        };
    }

    private void throwIfEmptyBodyResponse(Response.Body body) {
        if (body == null) {
            throw new OrderServiceUnavailableException("Order недоступен");
        }
    }
}
