package ru.yandex.practicum.exception.delivery;

public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException(String message) {
        super(message);
    }
}
