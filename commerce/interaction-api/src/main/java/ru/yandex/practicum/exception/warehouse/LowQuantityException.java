package ru.yandex.practicum.exception.warehouse;

public class LowQuantityException extends RuntimeException {

    public LowQuantityException(String message) {
        super(message);
    }
}