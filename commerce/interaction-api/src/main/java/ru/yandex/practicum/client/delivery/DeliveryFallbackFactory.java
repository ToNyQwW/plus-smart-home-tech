package ru.yandex.practicum.client.delivery;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.exception.delivery.DeliveryServiceUnavailableException;

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

            private DeliveryServiceUnavailableException defaultFallback(Throwable cause) {
                return new DeliveryServiceUnavailableException("Delivery недоступен: " + cause.getMessage());
            }
        };
    }
}
