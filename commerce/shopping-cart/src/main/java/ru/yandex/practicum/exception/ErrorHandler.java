package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedUserException.class)
    public ErrorResponse handleProductNotFoundException(NotAuthorizedUserException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception NotAuthorizedUserException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(UNAUTHORIZED)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.UNAUTHORIZED)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}