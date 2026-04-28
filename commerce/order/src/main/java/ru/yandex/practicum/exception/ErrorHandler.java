package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.exception.delivery.DeliveryServiceUnavailableException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(LowQuantityException.class)
    public ErrorResponse handleLowQuantityException(LowQuantityException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception LowQuantityException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.LOW_QUANTITY_IN_WAREHOUSE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception ProductNotFoundException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(NOT_FOUND)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.PRODUCT_NOT_FOUND)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(WarehouseServiceUnavailableException.class)
    public ErrorResponse handleWarehouseServiceUnavailableException(WarehouseServiceUnavailableException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception WarehouseServiceUnavailableException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(SERVICE_UNAVAILABLE)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.WAREHOUSE_SERVICE_UNAVAILABLE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

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

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(DeliveryServiceUnavailableException.class)
    public ErrorResponse handleDeliveryServiceUnavailableException(DeliveryServiceUnavailableException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception DeliveryServiceUnavailableException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(SERVICE_UNAVAILABLE)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.DELIVERY_SERVICE_UNAVAILABLE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}