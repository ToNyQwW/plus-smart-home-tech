package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.exception.delivery.DeliveryNotFoundException;
import ru.yandex.practicum.exception.delivery.InvalidDeliveryStateException;
import ru.yandex.practicum.exception.order.OrderServiceUnavailableException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.*;

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
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.DELIVERY_ALREADY_EXISTS)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(DeliveryNotFoundException.class)
    public ErrorResponse handleDeliveryNotFoundException(DeliveryNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception DeliveryNotFoundException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .httpStatus(NOT_FOUND)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.DELIVERY_NOT_FOUND)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidDeliveryStateException.class)
    public ErrorResponse handleInvalidDeliveryStateException(InvalidDeliveryStateException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception InvalidDeliveryStateException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.INVALID_DELIVERY_STATE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorResponse handleOrderNotFoundException(OrderNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception OrderNotFoundException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .httpStatus(NOT_FOUND)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.ORDER_NOT_FOUND)
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
                .httpStatus(SERVICE_UNAVAILABLE)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.WAREHOUSE_SERVICE_UNAVAILABLE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(OrderServiceUnavailableException.class)
    public ErrorResponse handleOrderServiceUnavailableException(OrderServiceUnavailableException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception OrderServiceUnavailableException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .httpStatus(SERVICE_UNAVAILABLE)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.ORDER_SERVICE_UNAVAILABLE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}
