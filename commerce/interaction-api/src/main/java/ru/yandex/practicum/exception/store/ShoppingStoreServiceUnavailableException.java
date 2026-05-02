package ru.yandex.practicum.exception.store;

public class ShoppingStoreServiceUnavailableException extends RuntimeException {

    public ShoppingStoreServiceUnavailableException(String message) {
        super(message);
    }
}
