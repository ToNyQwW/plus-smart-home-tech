package ru.yandex.practicum.exception.delivery;

public class DeliveryAlreadyExistsException extends RuntimeException {

    public DeliveryAlreadyExistsException(String message) {
        super(message);
    }
}
