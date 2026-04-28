package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.exception.cart.ShoppingCartNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedUserException.class)
    public ErrorResponse NotAuthorizedUserException(NotAuthorizedUserException e) {
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

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ErrorResponse handleShoppingCartNotFoundException(ShoppingCartNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception ShoppingCartNotFoundException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(NOT_FOUND)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.SHOPPING_CART_NOT_FOUND)
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        String exceptionMessage = e.getMessage();
        log.warn("Exception NoProductsInShoppingCartException, причина : {}", exceptionMessage);
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpStatus(BAD_REQUEST)
                .userMessage(exceptionMessage)
                .message(ErrorMessagesConstants.NO_PRODUCTS_IN_SHOPPING_CART)
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
}