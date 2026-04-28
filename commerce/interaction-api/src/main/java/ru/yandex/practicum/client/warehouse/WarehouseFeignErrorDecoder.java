package ru.yandex.practicum.client.warehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;

import java.io.InputStream;

import static ru.yandex.practicum.util.ErrorMessagesConstants.*;

@Component
public class WarehouseFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {

        try (InputStream inputStream = response.body().asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 400 -> mapWarehouseException(error);
                default -> new WarehouseServiceUnavailableException(error.getUserMessage());
            };
        }
    }

    private RuntimeException mapWarehouseException(ErrorResponse error) {
        return switch (error.getMessage()) {
            case PRODUCT_NOT_FOUND -> new ProductNotFoundException(error.getUserMessage());
            case PRODUCT_ALREADY_EXISTS -> new ProductAlreadyExistsException(error.getUserMessage());
            case LOW_QUANTITY_IN_WAREHOUSE -> new LowQuantityException(error.getUserMessage());
            default -> new RuntimeException(error.getUserMessage());
        };
    }
}
