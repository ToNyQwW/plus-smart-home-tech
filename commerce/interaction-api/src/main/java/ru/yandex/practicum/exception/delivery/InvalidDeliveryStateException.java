package ru.yandex.practicum.exception.delivery;

import ru.yandex.practicum.model.DeliveryState;

import java.util.UUID;

public class InvalidDeliveryStateException extends RuntimeException {

    public InvalidDeliveryStateException(UUID orderId, DeliveryState actual, DeliveryState expected) {
        super(String.format("Доставка с id: %s имеет статус: %s, ожидается: %s",
                orderId, actual, expected)
        );
    }
}
