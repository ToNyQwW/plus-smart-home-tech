package ru.yandex.practicum.client.warehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;

import java.io.InputStream;

@Component
public class WarehouseFeignErrorDecoder implements ErrorDecoder {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {

        try (InputStream inputStream = response.body().asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 400 -> new LowQuantityException(error.getUserMessage());
                default -> new WarehouseServiceUnavailableException(error.getUserMessage());
            };
        }
    }
}
