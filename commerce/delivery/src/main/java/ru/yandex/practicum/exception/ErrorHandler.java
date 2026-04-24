package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DeliveryAlreadyExistsException.class)
    public ErrorResponse handleDeliveryAlreadyExistsException(DeliveryAlreadyExistsException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception DeliveryAlreadyExistsException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.DELIVERY_ALREADY_EXISTS)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}
