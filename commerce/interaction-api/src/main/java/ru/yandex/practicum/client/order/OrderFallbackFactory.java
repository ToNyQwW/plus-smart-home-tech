package ru.yandex.practicum.client.order;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.exception.order.OrderServiceUnavailableException;

import java.util.UUID;

@Component
public class OrderFallbackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable cause) {
        return new OrderClient() {

            @Override
            public OrderDto paymentSuccess(UUID orderId) {
                if (cause instanceof OrderNotFoundException) {
                    throw (OrderNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public OrderDto paymentFailed(UUID orderId) {
                if (cause instanceof OrderNotFoundException) {
                    throw (OrderNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            private OrderServiceUnavailableException defaultFallback(Throwable cause) {
                return new OrderServiceUnavailableException("Order недоступен: " + cause.getMessage());
            }
        };
    }
}
