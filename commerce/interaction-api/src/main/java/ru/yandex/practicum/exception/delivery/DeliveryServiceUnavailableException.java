package ru.yandex.practicum.exception.delivery;

public class DeliveryServiceUnavailableException extends RuntimeException {

    public DeliveryServiceUnavailableException(String message) {
        super(message);
    }
}
