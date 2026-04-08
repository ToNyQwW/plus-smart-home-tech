package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ErrorResponse handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception ProductAlreadyExistsException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.PRODUCT_ALREADY_EXISTS)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}