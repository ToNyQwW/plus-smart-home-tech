package ru.yandex.practicum.client.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.payment.PaymentAlreadyExistsException;
import ru.yandex.practicum.exception.payment.PaymentServiceUnavailableException;
import ru.yandex.practicum.exception.store.ShoppingStoreServiceUnavailableException;

import java.io.InputStream;

import static ru.yandex.practicum.util.ErrorMessagesConstants.*;

@Component
@RequiredArgsConstructor
public class PaymentFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {
        Response.Body body = response.body();
        throwIfEmptyBodyResponse(body);

        try (InputStream inputStream = body.asInputStream()) {

            ErrorResponse error = objectMapper.readValue(inputStream, ErrorResponse.class);

            return switch (response.status()) {
                case 400, 404, 503 -> mapPaymentException(error);
                default -> new PaymentServiceUnavailableException(error.getUserMessage());
            };
        }
    }

    private RuntimeException mapPaymentException(ErrorResponse error) {
        return switch (error.getMessage()) {
            case PRODUCT_NOT_FOUND -> new ProductNotFoundException(error.getUserMessage());
            case PAYMENT_ALREADY_EXISTS -> new PaymentAlreadyExistsException(error.getUserMessage());
            case SHOPPING_STORE_SERVICE_UNAVAILABLE ->
                    new ShoppingStoreServiceUnavailableException(error.getUserMessage());
            default -> new RuntimeException(error.getUserMessage());
        };
    }

    private void throwIfEmptyBodyResponse(Response.Body body) {
        if (body == null) {
            throw new PaymentServiceUnavailableException("Payment недоступен");
        }
    }
}
