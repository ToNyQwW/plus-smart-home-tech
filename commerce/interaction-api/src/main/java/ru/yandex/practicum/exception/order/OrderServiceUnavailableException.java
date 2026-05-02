package ru.yandex.practicum.exception.order;

public class OrderServiceUnavailableException extends RuntimeException {

    public OrderServiceUnavailableException(String message) {
        super(message);
    }
}
