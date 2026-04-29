package ru.yandex.practicum.client.delivery;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.commerce.delivery.CalculateDeliveryCostRequest;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.exception.delivery.DeliveryNotFoundException;
import ru.yandex.practicum.exception.delivery.DeliveryServiceUnavailableException;
import ru.yandex.practicum.exception.delivery.InvalidDeliveryStateException;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DeliveryFallbackFactory implements FallbackFactory<DeliveryClient> {
    @Override
    public DeliveryClient create(Throwable cause) {
        return new DeliveryClient() {

            @Override
            public DeliveryDto createNewDelivery(CreateNewDeliveryRequest request) {
                if (cause instanceof DeliveryAlreadyExistsException) {
                    throw (DeliveryAlreadyExistsException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public DeliveryDto successfulDelivery(UUID orderId) {
                if (cause instanceof DeliveryNotFoundException) {
                    throw (DeliveryNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public DeliveryDto pickedDelivery(UUID orderId) {
                if (cause instanceof DeliveryNotFoundException) {
                    throw (DeliveryNotFoundException) cause;
                }
                if (cause instanceof InvalidDeliveryStateException) {
                    throw (InvalidDeliveryStateException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public DeliveryDto failedDelivery(UUID orderId) {
                if (cause instanceof DeliveryNotFoundException) {
                    throw (DeliveryNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public BigDecimal deliveryCost(CalculateDeliveryCostRequest request) {
                if (cause instanceof DeliveryNotFoundException) {
                    throw (DeliveryNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            private DeliveryServiceUnavailableException defaultFallback(Throwable cause) {
                return new DeliveryServiceUnavailableException("Delivery недоступен: " + cause.getMessage());
            }
        };
    }
}
