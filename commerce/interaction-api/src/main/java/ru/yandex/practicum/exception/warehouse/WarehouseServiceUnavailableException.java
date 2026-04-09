package ru.yandex.practicum.exception.warehouse;

public class WarehouseServiceUnavailableException extends RuntimeException {

    public WarehouseServiceUnavailableException(String message) {
        super(message);
    }
}