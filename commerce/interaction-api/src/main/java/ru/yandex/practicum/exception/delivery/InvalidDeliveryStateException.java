package ru.yandex.practicum.exception.delivery;

import ru.yandex.practicum.model.DeliveryState;

import java.util.UUID;

public class InvalidDeliveryStateException extends RuntimeException {

    public InvalidDeliveryStateException(String message) {
        super(message);
    }

    public InvalidDeliveryStateException(UUID orderId, DeliveryState actual, DeliveryState expected) {
        super(String.format("Доставка с id заказа: %s имеет статус: %s, ожидается: %s",
                orderId, actual, expected)
        );
    }
}
