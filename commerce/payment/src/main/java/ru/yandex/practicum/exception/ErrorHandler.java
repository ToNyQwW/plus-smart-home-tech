package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.order.OrderServiceUnavailableException;
import ru.yandex.practicum.exception.payment.PaymentAlreadyExistsException;
import ru.yandex.practicum.exception.payment.PaymentNotFoundException;
import ru.yandex.practicum.exception.store.ShoppingStoreServiceUnavailableException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ErrorResponse handlePaymentAlreadyExistsException(PaymentAlreadyExistsException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception PaymentAlreadyExistsException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.PAYMENT_ALREADY_EXISTS)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(PaymentNotFoundException.class)
    public ErrorResponse handlePaymentNotFoundException(PaymentNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception PaymentNotFoundException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(NOT_FOUND)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.PAYMENT_NOT_FOUND)
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

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorResponse handleOrderNotFoundException(OrderNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception OrderNotFoundException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(NOT_FOUND)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.ORDER_NOT_FOUND)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(ShoppingStoreServiceUnavailableException.class)
    public ErrorResponse handleShoppingStoreServiceUnavailableException(ShoppingStoreServiceUnavailableException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception ShoppingStoreServiceUnavailableException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(SERVICE_UNAVAILABLE)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.SHOPPING_STORE_SERVICE_UNAVAILABLE)
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
                .stackTrace(e.getStackTrace())
                .httpStatus(SERVICE_UNAVAILABLE)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.ORDER_SERVICE_UNAVAILABLE)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}
