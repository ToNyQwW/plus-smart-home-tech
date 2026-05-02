package ru.yandex.practicum.client.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.store.ShoppingStoreServiceUnavailableException;

import java.io.InputStream;

import static ru.yandex.practicum.util.ErrorMessagesConstants.PRODUCT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ShoppingStoreFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {
        Response.Body body = response.body();
        throwIfEmptyBodyResponse(body);

        try (InputStream inputStream = body.asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 404 -> mapShoppingStoreException(error);
                default -> new ShoppingStoreServiceUnavailableException(error.getUserMessage());
            };
        }
    }

    private RuntimeException mapShoppingStoreException(ErrorResponse error) {
        return switch (error.getMessage()) {
            case PRODUCT_NOT_FOUND -> new ProductNotFoundException(error.getUserMessage());
            default -> new RuntimeException(error.getUserMessage());
        };
    }

    private void throwIfEmptyBodyResponse(Response.Body body) {
        if (body == null) {
            throw new ShoppingStoreServiceUnavailableException("Shopping store недоступен");
        }
    }
}
