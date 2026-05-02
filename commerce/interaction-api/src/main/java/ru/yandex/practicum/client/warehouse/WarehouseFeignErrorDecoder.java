package ru.yandex.practicum.client.warehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;

import java.io.InputStream;

import static ru.yandex.practicum.util.ErrorMessagesConstants.*;

@Component
@RequiredArgsConstructor
public class WarehouseFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {
        Response.Body body = response.body();
        throwIfEmptyBodyResponse(body);

        try (InputStream inputStream = body.asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 400, 404 -> mapWarehouseException(error);
                default -> new WarehouseServiceUnavailableException(error.getUserMessage());
            };
        }
    }

    private RuntimeException mapWarehouseException(ErrorResponse error) {
        return switch (error.getMessage()) {
            case ORDER_NOT_FOUND -> new OrderNotFoundException(error.getUserMessage());
            case PRODUCT_NOT_FOUND -> new ProductNotFoundException(error.getUserMessage());
            case LOW_QUANTITY_IN_WAREHOUSE -> new LowQuantityException(error.getUserMessage());
            default -> new RuntimeException(error.getUserMessage());
        };
    }

    private void throwIfEmptyBodyResponse(Response.Body body) {
        if (body == null) {
            throw new WarehouseServiceUnavailableException("Warehouse недоступен");
        }
    }
}
