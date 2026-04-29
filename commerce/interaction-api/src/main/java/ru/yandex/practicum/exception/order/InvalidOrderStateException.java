package ru.yandex.practicum.exception.order;

import ru.yandex.practicum.model.OrderState;

import java.util.UUID;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String message) {
        super(message);
    }

    public InvalidOrderStateException(UUID orderId, OrderState actual) {
        super(String.format("Заказ с id: %s имеет статус: %s. Возврат с таким статусом не предусмотрен",
                orderId, actual)
        );
    }
}
